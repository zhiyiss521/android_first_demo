package com.zhiyi.android_first_demo.ui.mangaDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zhiyi.android_first_demo.databinding.MangaDetailCellMangaReviewBinding
import com.zhiyi.android_first_demo.databinding.MangaDetailCellRecommendedMangaBinding
import com.zhiyi.android_first_demo.model.MangaRecommendationItem
import com.zhiyi.android_first_demo.model.MangaReviewItem

class RecommendedMangaAdapter(
    private val onItemClick: (mangaId: Int) -> Unit
) : RecyclerView.Adapter<RecommendedMangaAdapter.ViewHolder>() {

    private var items = listOf<MangaRecommendationItem>()

    fun submitList(newList: List<MangaRecommendationItem>) {
        // 取前 10 条，避免横向列表过长影响性能
        this.items = newList.take(10)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MangaDetailCellRecommendedMangaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvRecommendedTitle.text = item.entry.title

        // 加载推荐漫画的封面
        Glide.with(holder.binding.ivRecommendedCover.context)
            .load(item.entry.images.jpg?.imageUrl)
            .placeholder(android.R.color.darker_gray)
            .into(holder.binding.ivRecommendedCover)

        // 点击卡片，再次跳转详情
        holder.itemView.setOnClickListener {
            onItemClick(item.entry.mal_id)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: MangaDetailCellRecommendedMangaBinding) :
        RecyclerView.ViewHolder(binding.root)
}

class MangaReviewsAdapter : RecyclerView.Adapter<MangaReviewsAdapter.ViewHolder>() {

    private var items = listOf<MangaReviewItem>()

    fun submitList(newList: List<MangaReviewItem>) {
        this.items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MangaDetailCellMangaReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.apply {
            tvReviewUserName.text = item.user.username
            // 裁剪截取一下日期字符串，只保留 YYYY-MM-DD
            tvReviewDate.text = if (item.date.length > 10) item.date.substring(0, 10) else item.date
            tvReviewScore.text = "★ ${item.score}"
            tvReviewContent.text = item.review
            tvReviewLikeCount.text = "👍 ${item.reactions.nice} 人觉得很赞"

            // 加载圆形头像
            Glide.with(ivReviewAvatar.context)
                .load(item.user.images.jpg.image_url)
                .placeholder(android.R.color.darker_gray)
                .into(ivReviewAvatar)
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: MangaDetailCellMangaReviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}