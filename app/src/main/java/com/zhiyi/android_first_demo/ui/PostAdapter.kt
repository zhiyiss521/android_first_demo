package com.zhiyi.android_first_demo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.zhiyi.android_first_demo.R
import com.zhiyi.android_first_demo.databinding.LayoutItemCellBinding // 假设你的 item 布局叫这个
import com.zhiyi.android_first_demo.model.ImagePost
import com.zhiyi.android_first_demo.model.UnsplashImage
import com.zhiyi.android_first_demo.util.LogUtil

// PostAdapter就是DioAdapter,是具体的子类适配器
class PostAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class PostViewHolder(val binding: LayoutItemCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setModel(model: UnsplashImage) {
            binding.tvDesc.text = "${model.alt_description}"
            binding.tvUserNickname.text = model.user.username
            binding.tvLike.text = model.likes.toString()
            if (model.liked_by_user) {
                binding.imageVHeart.setImageResource(R.drawable.ic_heart_red)
            } else {
                binding.imageVHeart.setImageResource(R.drawable.ic_heart_gray)
            }

            Glide.with(binding.root.context)
                .load(model.urls.small)
//                .placeholder(R.color.darker_gray)
                .into(binding.image)

            Glide.with(binding.root.context)
                .load(model.user.profile_image?.small)
//                .placeholder(R.color.darker_gray)
                .into(binding.imageVUserAvatar)

        }
    }

    var dataList = mutableListOf<UnsplashImage>()

    fun updateData(newData: List<UnsplashImage>) {
        val oldSize = dataList.size
        val newSize = newData.size

        if (oldSize == 0 || newSize <= oldSize) {
            dataList.clear()
            dataList.addAll(newData)
            notifyDataSetChanged()
        } else {
            val insertCount = newSize - oldSize
            val appendList = newData.subList(oldSize, newSize)
            dataList.addAll(appendList)
            notifyItemRangeInserted(oldSize, insertCount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
       if (viewType == TYPE_FOOTER) {
           val view = inflater.inflate(R.layout.item_list_footer, parent, false)
           return FooterViewHolder(view)
        } else {
            // 固定套路
            val binding = LayoutItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PostViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FooterViewHolder) {
            // 🌟 关键：因为是瀑布流，底部的加载栏如果只占一列会极丑。
            // 我们需要强行设置它的 LayoutParams，让它撑满全宽（横跨两列）
            val layoutParams = holder.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        } else if (holder is PostViewHolder){
            val item = dataList[position]
            holder.binding.root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
            holder.setModel(dataList[position])
        }
    }

    var onItemClickListener: ((UnsplashImage) -> Unit)? = null


    override fun getItemCount(): Int {
        return if (dataList.isEmpty()) 0 else dataList.size + 1
    }

    private val TYPE_ITEM = 0
    private val TYPE_FOOTER = 1
    override fun getItemViewType(position: Int): Int {
        return if (position == dataList.size) TYPE_FOOTER else TYPE_ITEM
    }
}