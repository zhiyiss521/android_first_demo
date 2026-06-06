package com.zhiyi.android_first_demo.model

import com.google.gson.annotations.SerializedName

data class HotListResponse(
    val success: Boolean,
    val title: String?,
    val subtitle: String?,
    val update_time: String?,
    val data: List<HotItem> = emptyList()
)

// 核心文章/热榜条目
data class HotItem(
    val index: Int,
    val title: String, // 标题
    val hot: String?,  // 热度值（如：50W，或者B站的播放量）
    val url: String    // 原文链接，可以直接用 WebView 打开
)

data class ZhihuHotResponse(
    val data: List<ZhihuHotItem> = emptyList()
)

// 2. 列表包裹层
data class ZhihuHotItem(
    val id: String?,
    val type: String?,
    val detail_text: String?, // 热度（例如："740 万热度"）
    val target: ZhihuTarget?,  // 核心内容
    val children: List<ZhihuChild>? // 下方的附属信息（如图片）
)

// 3. 核心内容层
data class ZhihuTarget(
    val id: Long,             // 问题 ID
    val title: String = "",   // 话题标题（如：“腾讯高管称今年大部分代码由 AI 生成”）
    val excerpt: String?,     // 话题导语/简介
    val type: String?,        // 业务类型
    val url: String?          // 网页链接
)

data class ZhihuChild(
    val type: String?,
    val thumbnail: String?    // 话题相关的预览图片 URL
)

// ################################################################################################################################################################################################
data class MangaResponse(
    val pagination: MangaPagination?,
    val data: List<MangaItem> = emptyList()
)
// 这个是SerializedName是GJSON，用来将current_page字段改成currentPage的
data class MangaPagination(
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("has_next_page") val hasNextPage: Boolean,
    @SerializedName("last_visible_page") val lastVisiblePage: Int
)

data class MangaItem(
    @SerializedName("mal_id") val malId: Int, // 漫画的全局唯一 ID
    val url: String?,                         // 漫画在平台的详情页网页链接
    val title: String = "",                   // 默认英文/罗马音标题（如 "Berserk"）
    val synopsis: String?,                    // 漫画剧情简介、导语
    val images: MangaImages?,                 // 各种规格的封面图片集合
    val type: String?,                        // 类别：Manga(漫画), Novel(小说), Doujinshi(同人志)
    val chapters: Int?,                       // 总话数（如果完结了的话）
    val score: Double?                        // 全球二次元漫迷给出的评分（例如 9.47）
)

data class MangaImages(
    val jpg: MangaImageUrls?,
    val webp: MangaImageUrls? // 推荐用 webp，体积小加载快
)

data class MangaImageUrls(
    @SerializedName("image_url") val imageUrl: String,             // 标准大小图片
    @SerializedName("small_image_url") val smallImageUrl: String,  // 缩略小图
    @SerializedName("large_image_url") val largeImageUrl: String   // 高清大图（放列表里这个最清晰）
)