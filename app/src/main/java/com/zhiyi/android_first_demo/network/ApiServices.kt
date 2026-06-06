package com.zhiyi.android_first_demo.network

import com.zhiyi.android_first_demo.model.HotListResponse
import com.zhiyi.android_first_demo.model.MangaResponse
import com.zhiyi.android_first_demo.model.ZhihuHotResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ZhihuApiService {
    // 知乎全网总热榜
    @GET("topstory/hot-lists/total")
    suspend fun getZhihuHot(): ZhihuHotResponse
}

interface MangaApiService {
    @GET("v4/top/manga")
    suspend fun getTopManga(): MangaResponse
}