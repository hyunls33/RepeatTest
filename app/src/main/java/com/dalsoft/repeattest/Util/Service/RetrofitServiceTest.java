package com.dalsoft.repeattest.Util.Service;

import com.dalsoft.repeattest.Util.Dto.Comments;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitServiceTest {

    @Headers("Content-Type: application/json")
    @GET("/comments")
    Observable<ArrayList<Comments>> getTestData(@Query("postId") int postId);

    @Headers("Content-Type: application/json")
    @GET("/comments")
    Observable<ArrayList<Comments>> getTestData(@Header("Authorization") String token, @Query("postId") int postId);
}
