package com.zhiyi.android_first_demo.ui.userDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhiyi.android_first_demo.databinding.ItemPhotoBinding
import com.zhiyi.android_first_demo.model.UnsplashPhoto

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val dataList = mutableListOf<UnsplashPhoto>()

    inner class PhotoViewHolder( val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val model = dataList[position]
        holder.binding.model = model
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun submitList(list: List<UnsplashPhoto>?) {
        dataList.clear()
        if (!list.isNullOrEmpty()) {
            dataList.addAll(list)
        }
        notifyDataSetChanged()
    }

}