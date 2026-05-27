package com.zhiyi.android_first_demo.ui

import android.content.res.Resources
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(android.R.drawable.progress_horizontal) // 正在加载时显示的图
            .error(android.R.drawable.stat_notify_error)       // 加载失败时显示的图
            .into(imageView)
    }
}

@BindingAdapter("imageRatio")
fun setImageRatio(view: ImageView, ratio: Float) {
    if (ratio <= 0f) return
    view.post {
        val width = view.width
        if (width <= 0) return@post
        val params = view.layoutParams
        params.height = (width * ratio).toInt()
        view.layoutParams = params
    }
}