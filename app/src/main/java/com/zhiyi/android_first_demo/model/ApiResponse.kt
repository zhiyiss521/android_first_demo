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

data class MangaFullDetailsResponse(
    val data: MangaFullDetails
)

data class MangaFullDetails(
    val mal_id: Int,
    val url: String,
    val images: MangaImages,
    val approved: Boolean,
    val titles: List<MangaTitle>,
    val title: String,
    val title_english: String?,
    val title_japanese: String?,
    val title_synonyms: List<String>,
    val type: String,
    val chapters: Int?,
    val volumes: Int?,
    val status: String,
    val publishing: Boolean,
    val published: PublishedPeriod,
    val score: Double?,
    val scored: Double?,
    val scored_by: Int?,
    val rank: Int?,
    val popularity: Int?,
    val members: Int?,
    val favorites: Int?,
    val synopsis: String?,
    val background: String?,
    val authors: List<MangaNode>,
    val serializations: List<MangaNode>,
    val genres: List<MangaNode>,
    val explicit_genres: List<MangaNode>,
    val themes: List<MangaNode>,
    val demographics: List<MangaNode>,
    val relations: List<MangaRelation>, // 关联作品（如：前作、续作、动画化衍生）
    val external: List<MangaExternalLink> // 核心：外部链接（包含看漫画的正版官方渠道！）
)

data class MangaNode(
    val mal_id: Int,
    val type: String,
    val name: String,
    val url: String
)

data class MangaTitle(
    val type: String, // "Default", "Japanese", "English" 等
    val title: String
)

data class PublishedPeriod(
    val from: String?,
    val to: String?,
    val string: String? // 可直接用于 UI 显示的格式化时间字符串，例如 "Jul 22, 1997 to ?"
)

data class MangaRelation(
    val relation: String, // 关系类型，例如 "Adaptation", "Side story"
    val entry: List<MangaNode>
)

data class MangaExternalLink(
    val name: String, // 网站名字，例如 "Official Site", "Wikipedia", "Manga Plus"
    val url: String  // 跳转的 H5 链接
)

data class MangaReviewsResponse(
    val data: List<MangaReviewItem>
)

data class MangaReviewItem(
    val mal_id: Int,
    val url: String,
    val type: String,
    val reactions: ReviewReactions, // 点赞、觉得有用的数量
    val date: String,             // 评论日期 (ISO8601 格式)
    val review: String,           // 核心：评论的长文本内容
    val score: Int,               // 用户给出的综合评分
    val is_spoiler: Boolean,      // 是否剧透（UI上可以做模糊或者警告提示）
    val user: ReviewUser          // 评论者用户信息
)

data class ReviewUser(
    val username: String,
    val url: String,
    val images: UserImages
)

data class UserImages(
    val jpg: UserImageSet
)

data class UserImageSet(
    val image_url: String // 用户头像 URL
)

data class ReviewReactions(
    val overall: Int,      // 总互动数
    val nice: Int,         // 觉得很赞的数量
    val love_it: Int,      // 喜欢的数量
    val funny: Int         // 觉得搞笑的数量
)

data class MangaRecommendationsResponse(
    val data: List<MangaRecommendationItem>
)

data class MangaRecommendationItem(
    val entry: RecommendedMangaEntry, // 被推荐的漫画简要信息
    val url: String,
    val votes: Int,                   // 有多少网友赞同这个推荐
    val context: String?               // 推荐原因/为什么推荐（例如："如果你喜欢热血和冒险，你一定不能错过这部..."）
)

data class RecommendedMangaEntry(
    val mal_id: Int,
    val url: String,
    val images: MangaImages, // 直接复用你列表页和详情页定义好的 MangaImages
    val title: String
)

// region  rawg
data class GamesResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Game>
)

data class Game(
    val id: Int,
    val name: String,
    val released: String?,
    val background_image: String?,
    val rating: Double,
    val metacritic: Int?,
    val ratings_count: Int
)

data class GameDetailResponse(
    val id: Int,
    val name: String,
    val description_raw: String?,
    val released: String?,
    val background_image: String?,
    val rating: Double,
    val metacritic: Int?,
    val website: String?,
    val developers: List<Developer>,
    val genres: List<Genre>,
    val publishers: List<Publisher>
)

data class Developer(
    val id: Int,
    val name: String
)

data class Genre(
    val id: Int,
    val name: String
)

data class Publisher(
    val id: Int,
    val name: String
)

data class ScreenshotsResponse(
    val count: Int,
    val results: List<Screenshot>
)

data class Screenshot(
    val id: Int,
    val image: String
)
//endregion

//region Steam

data class SteamHome(
    val specials: SteamSection?,
)

data class SteamSection(
    val id: String?,
    val name: String?,
    val items: List<SteamGameItem>?
)

data class SteamGameItem(

    val id: Long?,

    val name: String?,

    val header_image: String?,

    val large_capsule_image: String?,

    val small_capsule_image: String?,

    val discount_percent: Int?,

    val original_price: Long?,

    val final_price: Long?,

    val headline: String?,

    val body: String?,

    val url: String?,

    val windows_available: Boolean?,

    val mac_available: Boolean?,

    val linux_available: Boolean?,

    val controller_support: String?,

    val discount_expiration: Long?
)

//endregion