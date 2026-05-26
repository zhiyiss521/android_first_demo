package com.zhiyi.android_first_demo.ui

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zhiyi.android_first_demo.databinding.LayoutItemCellBinding // 假设你的 item 布局叫这个
import com.zhiyi.android_first_demo.model.ImagePost
import com.zhiyi.android_first_demo.model.UnsplashImage

// PostAdapter就是DioAdapter,是具体的子类适配器
class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: LayoutItemCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setModel(model: UnsplashImage) {
            binding.tvDesc.text = "${model.alt_description}"
            binding.tvUserNickname.text = model.user.username
            binding.tvLike.text = model.likes.toString()
            if (model.liked_by_user) {
//                binding.imageVHeart.setImageResource(R.drawable.ic)
            } else {
//                binding.imageVHeart.setImageResource(R.drawable.ic_heart_empty)
            }

            Glide.with(binding.root.context)
                .load(model.urls.small)
                .placeholder(R.color.darker_gray)
                .into(binding.image)

            Glide.with(binding.root.context)
                .load(model.user.profile_image.small)
                .placeholder(R.color.darker_gray)
                .into(binding.imageVUserAvatar)

        }
    }

    var dataList :List<UnsplashImage> = emptyList()
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

    var onItemClickListener: ((UnsplashImage) -> Unit)? = null

    override fun getItemCount(): Int = dataList.size
}