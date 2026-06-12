package com.zhiyi.android_first_demo.network

import com.zhiyi.android_first_demo.model.GameDetailResponse
import com.zhiyi.android_first_demo.model.GamesResponse
import com.zhiyi.android_first_demo.model.HotListResponse
import com.zhiyi.android_first_demo.model.MangaFullDetailsResponse
import com.zhiyi.android_first_demo.model.MangaRecommendationsResponse
import com.zhiyi.android_first_demo.model.MangaResponse
import com.zhiyi.android_first_demo.model.MangaReviewsResponse
import com.zhiyi.android_first_demo.model.ScreenshotsResponse
import com.zhiyi.android_first_demo.model.SteamHome
import com.zhiyi.android_first_demo.model.SteamSection
import com.zhiyi.android_first_demo.model.ZhihuHotResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// 知乎
interface ZhihuApiService {
    @GET("topstory/hot-lists/total")
    suspend fun getZhihuHot(): ZhihuHotResponse
}

// 漫画
interface MangaApiService {
    @GET("v4/top/manga")
    suspend fun getTopManga(
        @Query("page") page: Int = 1,                 // 当前页码
        @Query("type") type: String? = null,           // 可选：过滤类型 (如 "manga", "novel")
        @Query("filter") filter: String? = null
    ): MangaResponse

    @GET("v4/manga/{id}/full")
    suspend fun getMangaFullDetails(
        @Path("id") id: Int
    ): MangaFullDetailsResponse

    @GET("v4/manga/{id}/reviews")
    suspend fun getMangaReviews(
        @Path("id") id: Int
    ): MangaReviewsResponse

    @GET("v4/manga/{id}/recommendations")
    suspend fun getMangaRecommendations(
        @Path("id") id: Int
    ): MangaRecommendationsResponse

}

// 游戏
interface RawgApiService{

    @GET("games")
    suspend fun getGames(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("ordering") ordering: String? = "-rating"
    ): GamesResponse


    @GET("games")
    suspend fun searchGames(
        @Query("key") apiKey: String,
        @Query("search") keyword: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): GamesResponse


    @GET("games/{id}")
    suspend fun getGameDetail(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): GameDetailResponse

    @GET("games/{id}/screenshots")
    suspend fun getGameScreenshots(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): ScreenshotsResponse

    @GET("games/{id}/game-series")
    suspend fun getGameSeries(
        @Path("id") gameId: Int,
        @Query("key") apiKey: String
    ): GamesResponse


}

interface SteamApiService {

    @GET("featuredcategories")
    suspend fun getFeaturedCategories(): SteamHome

}