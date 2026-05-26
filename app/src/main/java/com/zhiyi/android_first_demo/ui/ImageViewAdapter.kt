package com.zhiyi.android_first_demo.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(url)
            // 下面两行是防呆设计，可以先用系统自带的图标当占位符
            .placeholder(android.R.drawable.progress_horizontal) // 正在加载时显示的图
            .error(android.R.drawable.stat_notify_error)       // 加载失败时显示的图
            .into(imageView)
    }
}