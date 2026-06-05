package com.zhiyi.android_first_demo.model

data class GNewsResponse(
    val totalArticles: Int,
    val articles: List<GArticle>
)

data class GArticle(
    val title: String,
    val description: String,
    val content: String,
    val url: String,
    val image: String, // 注意：GNews 里叫 image
    val publishedAt: String
)