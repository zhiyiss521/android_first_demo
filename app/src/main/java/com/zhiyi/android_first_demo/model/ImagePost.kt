package com.zhiyi.android_first_demo.model

import com.google.gson.annotations.SerializedName

data class ImagePost (

    var id :String, // id
    val author: String, // 作者名字
    val width: Int,
    val height: Int,
    val url: String, // 图片
    @SerializedName("download_url")
    val downloadUrl: String // 🚀 真正的图片高清直链 URL

)