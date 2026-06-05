package com.zhiyi.android_first_demo.network

import com.zhiyi.android_first_demo.model.UnsplashImage
import com.zhiyi.android_first_demo.model.UnsplashUser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApiService {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
    ): List<UnsplashImage>

    @GET("photos/{id}")
    suspend fun getPhotoDetail(
        @Path("id") id: String
    ): UnsplashImage

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): UnsplashUser

}