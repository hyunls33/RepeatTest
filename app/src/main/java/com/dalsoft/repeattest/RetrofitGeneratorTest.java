package com.dalsoft.repeattest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitGeneratorTest {
    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder requestBuilder = chain.request().newBuilder();

                        requestBuilder.addHeader("Authorization", "사용할 토큰");

                        return chain.proceed(requestBuilder.build());
                    }
                })
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS);

    static String baseUrl = "http://jsonplaceholder.typicode.com/";

    static RetrofitServiceTest getService() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 요청결과를 RxJava의 Observable로 전달받음
                .addConverterFactory(GsonConverterFactory.create())        // Json형태의 결과를 원하는 형태의 class로 변환
                .build()
                .create(RetrofitServiceTest.class);
    }
}
