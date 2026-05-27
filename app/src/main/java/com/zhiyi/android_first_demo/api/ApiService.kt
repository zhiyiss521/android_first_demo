package com.zhiyi.android_first_demo.api

import com.zhiyi.android_first_demo.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("https://api.unsplash.com/photos")
    suspend fun getPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
    ): List<UnsplashImage>

    @GET("https://api.unsplash.com/photos/{id}")
    suspend fun getPhotoDetail(
        @Path("id") id: String
    ): UnsplashImage

}