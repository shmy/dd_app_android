package tech.shmy.dd_app.util;


import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.shmy.dd_app.defs.AfterResponse;
import tech.shmy.dd_app.entity.ErrorResponseEntity;
import tech.shmy.dd_app.entity.UserEntity;
import tech.shmy.dd_app.entity.VideoEntity;
import tech.shmy.dd_app.event.UserLogoutEvent;
import tech.shmy.dd_app.event.UserToLogin;
import tech.shmy.dd_app.service.UserService;
import tech.shmy.dd_app.service.VideoService;

import static tech.shmy.dd_app.defs.Env.API_BASE_URL;

public class HttpClient {
    private static OkHttpClient _client;
    private static VideoService _videoService;
    private static UserService _userService;
    private static Activity activity;

    private static VideoService getVideoService() {
        if (_videoService == null) {
            synchronized (Retrofit.class) {
                HttpClient.initService();
            }
        }
        return _videoService;
    }

    private static UserService getUserService() {
        if (_userService == null) {
            synchronized (Retrofit.class) {
                HttpClient.initService();
            }
        }
        return _userService;
    }

    private static OkHttpClient getClient() {
        if (_client == null) {
            synchronized (OkHttpClient.class) {
                _client = new OkHttpClient().newBuilder()
                        .addNetworkInterceptor(HttpClient::TokenIntercept)
                        .addInterceptor(HttpClient::StatusCodeIntercept)
                        .build();

            }
        }


        return _client;
    }

    private static void initService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder().client(HttpClient.getClient())
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        _videoService = retrofit.create(VideoService.class);
        _userService = retrofit.create(UserService.class);
    }

    public static void init(Activity activity) {
        HttpClient.activity = activity;
    }

    public static AfterResponse<List<VideoEntity>> getHotVideoList(int id) {
        try {
            List<VideoEntity> videoEntities = HttpClient.getVideoService()
                    .getHotVideoList(id)
                    .execute().body();
            return new AfterResponse<>(videoEntities);
        } catch (IOException e) {
            Error error = new Error(e.getMessage());
            return new AfterResponse<>(error);
        }
    }

    public static AfterResponse<VideoEntity> getVideoDetail(long id) {
        VideoEntity cacheVideo = CacheManager.getCacheVideo(id);
        if (cacheVideo != null) {
            return new AfterResponse<>(cacheVideo);
        }
        try {
            VideoEntity data = HttpClient.getVideoService()
                    .getVideoDetail(id)
                    .execute().body();
            return new AfterResponse<>(CacheManager.setCacheVideo(data));
        } catch (IOException e) {
            Error error = new Error(e.getMessage());
            return new AfterResponse<>(error);
        }
    }

    public static AfterResponse<List<VideoEntity>> getVideoList(int id, int page) {
        try {
            List<VideoEntity> data = HttpClient.getVideoService()
                    .getVideoList(id, page, 20)
                    .execute().body();
            return new AfterResponse<>(data);
        } catch (IOException e) {
            Error error = new Error(e.getMessage());
            return new AfterResponse<>(error);
        }
    }

    public static AfterResponse<List<VideoEntity>> getSearchList(String keyword, int page, int per_page) {
        try {
            List<VideoEntity> data = HttpClient.getVideoService()
                    .getSearchList(keyword, page, 20)
                    .execute().body();
            return new AfterResponse<>(data);
        } catch (IOException e) {
            Error error = new Error(e.getMessage());
            return new AfterResponse<>(error);
        }
    }

    public static AfterResponse<List<VideoEntity>> getRandList() {
        try {
            List<VideoEntity> data = HttpClient.getVideoService()
                    .getRandList()
                    .execute().body();
            return new AfterResponse<>(data);
        } catch (IOException e) {
            Error error = new Error(e.getMessage());
            return new AfterResponse<>(error);
        }
    }

    public static AfterResponse<String> userLogin(String username, String password) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("username", username);
            body.put("password", password);
            String data = HttpClient.getUserService()
                    .login(body)
                    .execute().body();
            return new AfterResponse<>(data);
        } catch (IOException e) {
            Error error = new Error(e.getMessage());
            return new AfterResponse<>(error);
        }
    }

    public static AfterResponse<UserEntity> userRegister(String username, String password, String re_password) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("username", username);
            body.put("password", password);
            body.put("re_password", re_password);
            UserEntity data = HttpClient.getUserService()
                    .register(body)
                    .execute().body();
            return new AfterResponse<>(data);
        } catch (IOException e) {
            Error error = new Error(e.getMessage());
            return new AfterResponse<>(error);
        }
    }

    public static AfterResponse<UserEntity> userInfo() {
        try {
            UserEntity data = HttpClient.getUserService()
                    .me()
                    .execute().body();
            return new AfterResponse<>(data);
        } catch (IOException e) {
            Error error = new Error(e.getMessage());
            return new AfterResponse<>(error);
        }
    }

    @NonNull
    private static Response StatusCodeIntercept(Interceptor.Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        int code = response.code();
        if (code != 200) {
            assert response.body() != null;
            String resString = response.body().string();
            try {
                ErrorResponseEntity errorResponseEntity = new Gson().fromJson(resString, ErrorResponseEntity.class);
                resString = errorResponseEntity.info;
            } catch (Exception ignored) {
            }
            String finalResString = resString;
            HttpClient.activity.runOnUiThread(() -> {
                Toasty.warning(HttpClient.activity, finalResString, Toast.LENGTH_SHORT, true).show();
            });
            if (code == 401) {
                EventBus.getDefault().post(new UserLogoutEvent());
                EventBus.getDefault().post(new UserToLogin());
            }
            throw new IOException(resString);
        }
        return response;
    }

    private static Response TokenIntercept(Interceptor.Chain chain) throws IOException {
        System.out.println("-----------");
        System.out.println(chain.request().url());
        String token = Util.mmkv.decodeString("token");
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("App-Platform", "2")
                .build();
        return chain.proceed(request);
    }
}
