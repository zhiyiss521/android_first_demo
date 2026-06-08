package com.zhiyi.android_first_demo.network

import com.zhiyi.android_first_demo.model.HotListResponse
import com.zhiyi.android_first_demo.model.MangaFullDetailsResponse
import com.zhiyi.android_first_demo.model.MangaRecommendationsResponse
import com.zhiyi.android_first_demo.model.MangaResponse
import com.zhiyi.android_first_demo.model.MangaReviewsResponse
import com.zhiyi.android_first_demo.model.ZhihuHotResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ZhihuApiService {
    // 知乎全网总热榜
    @GET("topstory/hot-lists/total")
    suspend fun getZhihuHot(): ZhihuHotResponse
}

interface MangaApiService {
    @GET("v4/top/manga")
    suspend fun getTopManga(
        @Query("page") page: Int = 1,                 // 当前页码
        @Query("type") type: String? = null,           // 可选：过滤类型 (如 "manga", "novel")
        @Query("filter") filter: String? = null
    ): MangaResponse

    // 2. 核心：获取单部漫画的完整详细信息（简介、评分、出版状态等）
    // 请求示例：v4/manga/{id}/full
    @GET("v4/manga/{id}/full")
    suspend fun getMangaFullDetails(
        @Path("id") id: Int
    ): MangaFullDetailsResponse

    // 3. 扩展：获取这部漫画底下的精选用户评论（做详情页的评论区评论特别好用）
    // 请求示例：v4/manga/{id}/reviews
    @GET("v4/manga/{id}/reviews")
    suspend fun getMangaReviews(
        @Path("id") id: Int
    ): MangaReviewsResponse

    // 4. 扩展：获取该漫画的推荐关联作品（猜你喜欢/相关推荐）
    // 请求示例：v4/manga/{id}/recommendations
    @GET("v4/manga/{id}/recommendations")
    suspend fun getMangaRecommendations(
        @Path("id") id: Int
    ): MangaRecommendationsResponse


}