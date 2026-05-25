package com.zhiyi.android_first_demo.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * 🌟 这是一个【顶层函数】（没有写在任何 class 关键字里面）。
 * 这样 Data Binding 编译器才能在全球范围内一眼找到它！
 * * 当你在 XML 里写 app:imageUrl 时，系统会自动把 ImageView 实例和字符串网址传进这个函数。
 */
@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    // 如果网址不为空，就用 Glide 去异步下载图片
    if (!url.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(url)
            // 下面两行是防呆设计，可以先用系统自带的图标当占位符
            .placeholder(android.R.drawable.progress_horizontal) // 正在加载时显示的图
            .error(android.R.drawable.stat_notify_error)       // 加载失败时显示的图
            .into(imageView)
    }
}