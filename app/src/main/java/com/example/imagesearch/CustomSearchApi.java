package com.example.imagesearch;

import com.example.imagesearch.model.Response;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CustomSearchApi {

    @GET("/customsearch/v1")
    Flowable<Response> getData(@Query("key") String key,
                               @Query("cx") String cx,
                               @Query("q") String query,
                               @Query("searchType") String searchType,
                               @Query("fileType") String fileType);
}
