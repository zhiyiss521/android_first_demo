package com.zhiyi.android_first_demo.model

import com.google.gson.annotations.SerializedName

// 1. 最外层的包裹响应
data class NewsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<Article>
)

// 2. 单条文章的数据结构
data class Article(
    @SerializedName("source") val source: Source,
    @SerializedName("author") val author: String?,      // 有可能为 null
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?, // 有可能为 null
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String?, // 有可能为 null
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("content") val content: String?    // 有可能为 null
)

// 3. 文章来源数据结构
data class Source(
    @SerializedName("id") val id: String?,             // 你的 JSON 里好多都是 null
    @SerializedName("name") val name: String
)