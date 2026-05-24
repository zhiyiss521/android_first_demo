package com.zhiyi.android_first_demo.ui

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zhiyi.android_first_demo.databinding.LayoutItemCellBinding // 假设你的 item 布局叫这个
import com.zhiyi.android_first_demo.model.ImagePost

// PostAdapter就是DioAdapter,是具体的子类适配器
class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: LayoutItemCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setModel(model: ImagePost) {
            binding.tvAuthor.text = "By: ${model.author}"
            Glide.with(binding.root.context)
                .load(model.downloadUrl)                  // 图片的网络直链
                .placeholder(R.color.darker_gray) // 图片下载出来前的占位灰色块
                .into(binding.image)
        }
    }

    var dataList :List<ImagePost> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged() // 刷新列表
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        // 固定套路
        val binding = LayoutItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.root.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
        holder.setModel(dataList[position])
    }

    var onItemClickListener: ((ImagePost) -> Unit)? = null

    override fun getItemCount(): Int = dataList.size
}