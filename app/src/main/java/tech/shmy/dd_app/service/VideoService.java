package tech.shmy.dd_app.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tech.shmy.dd_app.entity.VideoEntity;

public interface VideoService {
    @GET("video/{pid}")
    Call<List<VideoEntity>> getVideoList(@Path("pid") int id, @Query("page") int page, @Query("per_page") int per_page);

    @GET("video/hot")
    Call<List<VideoEntity>> getHotVideoList(@Query("pid") int id);

    @GET("video/detail/{id}")
    Call<VideoEntity> getVideoDetail(@Path("id") int id);

    @GET("video/search")
    Call<List<VideoEntity>> getSearchList(@Query("keyword") String keyword, @Query("page") int page, @Query("per_page") int per_page);

    @GET("video/rand")
    Call<List<VideoEntity>> getRandList();

}
