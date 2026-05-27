package com.zhiyi.android_first_demo.model

data class UnsplashImage (
    val id: String,
    val width: Int,
    val height: Int,
    val color: String, // 👈 图片主色调，UI 体验利器！
    val description: String?,
    val urls: ImageUrls, // 包含 raw, full, regular, small, thumb 等多种尺寸
    val user: UnsplashUser, // 摄影师信息
    val likes: Int,
    val alt_description:String,
    val views:Int,
    val downloads:Int,
    val liked_by_user:Boolean,
    val exif:UnsplashExif,
)

data class ImageUrls(val regular: String, val small: String,val raw: String,val full: String,val thumb: String,val small_s3: String)

data class UnsplashUser(
    val id: String,
    val updated_at: String?,
    val username: String?,
    val name: String?,
    val first_name: String?,
    val last_name: String?,
    val twitter_username: String?,
    val portfolio_url: String?,
    val bio: String?,
    val location: String?,
    val links: UnsplashUserLinks?,
    val profile_image: UnsplashUserProfileImage?,
    val instagram_username: String?,
    val total_collections: Int = 0,
    val total_likes: Int = 0,
    val total_photos: Int = 0,
    val total_free_photos: Int = 0,
    val total_promoted_photos: Int = 0,
    val total_illustrations: Int = 0,
    val total_free_illustrations: Int = 0,
    val total_promoted_illustrations: Int = 0,
    val accepted_tos: Boolean = false,
    val for_hire: Boolean = false,
    val social: UnsplashUserSocial?
)

data class UnsplashUserLinks(
    val self: String?,
    val html: String?,
    val photos: String?,
    val likes: String?
)

data class UnsplashUserProfileImage(val small: String,val medium:String,val large:String)

data class UnsplashUserSocial(
    val instagram_username: String?,
    val portfolio_url: String?,
    val twitter_username: String?,
    val paypal_email: String?
)

data class UnsplashExif(
     val make:String?,
     val model:String?,
     val name:String?,
     val exposure_time:String?,
     val aperture:String?,
     val focal_length:String?,
     val iso:Int
)