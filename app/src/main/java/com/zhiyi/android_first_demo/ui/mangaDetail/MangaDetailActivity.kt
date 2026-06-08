package com.zhiyi.android_first_demo.ui.mangaDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zhiyi.android_first_demo.R
import com.zhiyi.android_first_demo.databinding.ActivityMangaDetailBinding
import com.zhiyi.android_first_demo.network.ApiClient
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MangaDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMangaDetailBinding
    private var mangaId: Int = -1

    // 声明两个子模块的 Adapter
    private lateinit var recommendedAdapter: RecommendedMangaAdapter
    private lateinit var reviewsAdapter: MangaReviewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMangaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 设置 Toolbar 返回键
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.toolbar.title = ""
        
        // 2. 接收列表页传过来的唯一漫画 ID
        mangaId = intent.getIntExtra("manga_id", -1)
        if (mangaId == -1) {
            Toast.makeText(this, "未找到该漫画ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 3. 垫底 UI：如果列表页传了标题，一进来先展示，防止白屏白字体验差
        val initialTitle = intent.getStringExtra("manga_title")
        if (!initialTitle.isNullOrEmpty()) {
            binding.tvMangaTitle.text = initialTitle
        }

        // 4. 初始化两个 RecyclerView
        initRecyclerViews()

        // 5. 核心：并发请求接口数据
        loadMangaDetails()
    }

    private fun initRecyclerViews() {
        // 横向“猜你喜欢”列表
        recommendedAdapter = RecommendedMangaAdapter { nextMangaId ->
            // 支持点击推荐“套娃”跳转到自己
            val intent = Intent(this, MangaDetailActivity::class.java).apply {
                putExtra("manga_id", nextMangaId)
            }
            startActivity(intent)
        }
        binding.rvRecommendations.apply {
            layoutManager = LinearLayoutManager(this@MangaDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendedAdapter
        }

        // 纵向“用户评论”列表
        reviewsAdapter = MangaReviewsAdapter()
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(this@MangaDetailActivity, LinearLayoutManager.VERTICAL, false)
            adapter = reviewsAdapter
        }
    }

    private fun loadMangaDetails() {
        lifecycleScope.launch {
            try {
                // 可在此处展示全局 Loading 动画

                // 🔥 利用 async 发起三路网络并发，节省多倍等待时间
                val detailsDeferred = async { ApiClient.mangaService.getMangaFullDetails(mangaId) }
                val reviewsDeferred = async { ApiClient.mangaService.getMangaReviews(mangaId) }
                val recommendsDeferred = async { ApiClient.mangaService.getMangaRecommendations(mangaId) }

                // 同步等待三个接口的响应结果
                val detailsData = detailsDeferred.await().data
                val reviewsData = reviewsDeferred.await().data
                val recommendsData = recommendsDeferred.await().data

                // 统一开始渲染 UI 模块
                binding.tvMangaTitle.text = detailsData.title
                binding.tvMangaAuthor.text = detailsData.authors.firstOrNull()?.name ?: "Unknown Author"
                binding.tvSynopsis.text = detailsData.synopsis ?: "暂无简介"

                // 渲染：真实封面
                Glide.with(this@MangaDetailActivity)
                    .load(detailsData.images.jpg?.imageUrl)
                    .into(binding.ivRealCover)

                // 渲染：高斯模糊背景大图（利用高斯模糊库）
                Glide.with(this@MangaDetailActivity)
                    .load(detailsData.images.jpg?.largeImageUrl)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                    .into(binding.ivBlurBackground)

                // 绑定“去阅读”跳转逻辑
                setupReadButton(detailsData.external)

                // 填充子列表数据
                recommendedAdapter.submitList(recommendsData)
                reviewsAdapter.submitList(reviewsData)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MangaDetailActivity, "数据加载失败: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupReadButton(externalLinks: List<com.zhiyi.android_first_demo.model.MangaExternalLink>) {
        // 优先寻找带有 "Manga Plus" 或 "Official" 关键字的官方正版链接
        val targetLink = externalLinks.firstOrNull {
            it.name.contains("Manga Plus", ignoreCase = true) || it.name.contains("Official", ignoreCase = true)
        } ?: externalLinks.firstOrNull() // 找不到就用第一个链接兜底

        binding.btnReadManga.setOnClickListener {
            if (targetLink != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetLink.url))
                startActivity(intent)
            } else {
                Toast.makeText(this, "暂无外部官方阅读渠道", Toast.LENGTH_SHORT).show()
            }
        }
    }
}