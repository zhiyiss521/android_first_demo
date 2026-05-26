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
    var liked_by_user:Boolean
)

data class ImageUrls(val regular: String, val small: String)
data class UnsplashUser(
    var id :String,
    val name: String,
    val username: String,
    var profile_image:UnsplashUserProfileImage
)
data class UnsplashUserProfileImage(val small: String,val medium:String,val large:String)

data class UnsplashSearchResponse(val total: Int, val results: List<UnsplashImage>)