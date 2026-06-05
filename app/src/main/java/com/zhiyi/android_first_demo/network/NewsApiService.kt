package com.zhiyi.android_first_demo.network

import com.zhiyi.android_first_demo.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/top-headlines")
    suspend fun getTechNews(
        @Query("category") category: String = "technology",
        @Query("language") language: String = "en"
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") keyword: String,
        @Query("from") from: String? = null,
        @Query("sortBy") sortBy: String = "popularity"
    ): NewsResponse
}