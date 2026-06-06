package com.zhiyi.android_first_demo.ui.baseList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.zhiyi.android_first_demo.R
import com.zhiyi.android_first_demo.databinding.LayoutItemCellBinding // 假设你的 item 布局叫这个
import com.zhiyi.android_first_demo.model.UnsplashImage

// PostAdapter就是DioAdapter,是具体的子类适配器
class BaseListCellAdapter(
    private val dataType: ListDataType // 子类独有的属性
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                .into(binding.image)

            Glide.with(binding.root.context)
                .load(model.user.profile_image?.small)
                .into(binding.imageVUserAvatar)

        }
    }

    var dataList = mutableListOf<Any>()

    fun updateData(newData: List<Any>) {
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
        return when (viewType) {
            TYPE_FOOTER -> {
                val view = inflater.inflate(R.layout.item_list_footer, parent, false)
                FooterViewHolder(view)
            }

            ListDataType.UnsplashImage.ordinal -> {
                val binding = LayoutItemCellBinding.inflate(inflater, parent, false)
                PostViewHolder(binding)
            }
//            ListDataType.MANGA.ordinal -> {
//                val binding = LayoutItemMangaBinding.inflate(inflater, parent, false)
//                MangaViewHolder(binding)
//            }
            else -> throw IllegalArgumentException("未知的布局类型，请检查 ListDataType 枚举")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FooterViewHolder) {
            val layoutParams = holder.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        } else {
            val item = dataList[position]
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(item)
            }

            when (holder) {
                is PostViewHolder -> holder.setModel(item as com.zhiyi.android_first_demo.model.UnsplashImage)
//                is MangaViewHolder -> holder.setModel(item as com.zhiyi.android_first_demo.model.MangaItem)
            }
        }
    }


    var onItemClickListener: ((Any) -> Unit)? = null

    override fun getItemCount(): Int {
        return if (dataList.isEmpty()) 0 else dataList.size + 1
    }

    private val TYPE_FOOTER = -1
    override fun getItemViewType(position: Int): Int {
        return if (position == dataList.size) TYPE_FOOTER else dataType.ordinal
    }
}