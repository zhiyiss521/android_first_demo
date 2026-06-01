package com.zhiyi.android_first_demo.ui

import android.content.res.Resources
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.BlurTransformation

@BindingAdapter(value = ["imageUrl","blurRadius"], requireAll = false)
fun loadImage(imageView: ImageView, url: String?,blurRadius: Int?) {
    if (!url.isNullOrEmpty()) {
        val radius = blurRadius ?: 0
        Glide.with(imageView.context)
            .load(url)
            .placeholder(android.R.drawable.progress_horizontal) // 正在加载时显示的图
            .error(android.R.drawable.stat_notify_error)
            .apply {
                if (radius > 0) {
                    transform(
                        BlurTransformation(
                            radius,
                            4
                        )
                    )
                }
            }
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