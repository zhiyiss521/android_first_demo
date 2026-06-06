package com.zhiyi.android_first_demo.network

import com.zhiyi.android_first_demo.util.NetworkManager

object ApiClient {

    private const val UNSPLASH_BASE_URL = "https://api.unsplash.com/"
    private val unsplashHeaders = mapOf(
        "Authorization" to "Client-ID -sncfr6j20Nw3vWh5vY2JSAti-AS-X3d1OKoHk9pgJo"
    )

    val unsplashService: UnsplashApiService by lazy {
        NetworkManager.createService(
            baseUrl = UNSPLASH_BASE_URL,
            serviceClass = UnsplashApiService::class.java,
            customHeaders = unsplashHeaders // 👈 只给 Unsplash 挂载 Client-ID
        )
    }


    private const val NEWS_BASE_URL = "https://newsapi.org/"
    private val newsHeaders = mapOf(
        "X-Api-Key" to "1cd37d2b6e1e4aca9fe52fdc05c9c88b"
    )

    val newsService: NewsApiService by lazy {
        NetworkManager.createService(
            baseUrl = NEWS_BASE_URL,
            serviceClass = NewsApiService::class.java,
            customHeaders = newsHeaders,
        )
    }

    private const val GNEWS_BASE_URL = "https://gnews.io/"
    private val gnewsHeaders = mapOf(
        "X-Api-Key" to "bc95b89f989b3b87a663bdf086645e42"
    )

    val gNewsService: GNewsApiService by lazy {
        NetworkManager.createService(
            baseUrl = GNEWS_BASE_URL,
            serviceClass = GNewsApiService::class.java,
            customHeaders = gnewsHeaders,
        )
    }

    private const val HAN_BASE_URL = "https://api.zhihu.com/"

    val hanService: ZhihuApiService by lazy {
        NetworkManager.createService(
            baseUrl = HAN_BASE_URL,
            serviceClass = ZhihuApiService::class.java,
        )
    }


    val mangaService: MangaApiService by lazy {
        NetworkManager.createService(
            baseUrl = "https://api.jikan.moe/",
            serviceClass = MangaApiService::class.java,
        )
    }

}