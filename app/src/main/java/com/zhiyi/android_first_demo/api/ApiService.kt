package com.zhiyi.android_first_demo.api

import com.zhiyi.android_first_demo.model.ImagePost
import com.zhiyi.android_first_demo.model.Post
import com.zhiyi.android_first_demo.model.UnsplashImage
import com.zhiyi.android_first_demo.model.UnsplashSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("https://api.unsplash.com/photos")
    suspend fun getPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 1,
        @Query("order_by") orderBy: String = "latest" // latest: 最新, popular: 最热
    ): List<UnsplashImage>

    @GET("https://api.unsplash.com/photos/{id}")
    suspend fun getPhotoDetail(
        @Path("id") id: String
    ): UnsplashImage

    @GET("https://api.unsplash.com/search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 5
    ): UnsplashSearchResponse

}