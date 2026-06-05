package com.zhiyi.android_first_demo.network

import com.zhiyi.android_first_demo.model.GNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query



interface GNewsApiService {
    @GET("api/v4/search")
    suspend fun searchNews(
        @Query("q") keyword: String,
        @Query("lang") language: String = "zh", // 支持中文 "zh" 或英文 "en"
    ): GNewsResponse
}
