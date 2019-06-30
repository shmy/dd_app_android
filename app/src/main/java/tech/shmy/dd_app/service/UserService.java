package tech.shmy.dd_app.service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tech.shmy.dd_app.entity.UserEntity;

public interface UserService {
    @POST("user/login")
    Call<String> login(@Body() Map<String, String> body);
    @POST("user/register")
    Call<UserEntity> register(@Body() Map<String, String> body);
    @GET("user/me")
    Call<UserEntity> me();
}

