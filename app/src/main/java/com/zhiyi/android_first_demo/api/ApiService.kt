package com.zhiyi.android_first_demo.api

import com.zhiyi.android_first_demo.model.ImagePost
import com.zhiyi.android_first_demo.model.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("https://picsum.photos/v2/list")
    suspend fun getImageList(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): List<ImagePost>

    // 还是挺好的，不用自己拼接，自带path这个属性
    @GET("https://picsum.photos/id/{id}/info")
    suspend fun getImageDetail(
        @Path("id") imageId: String
    ): ImagePost

}