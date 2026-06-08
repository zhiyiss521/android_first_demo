package com.zhiyi.android_first_demo.ui.baseList

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zhiyi.android_first_demo.R
import com.zhiyi.android_first_demo.databinding.FragmentBaseListBinding
import com.zhiyi.android_first_demo.model.MangaItem
import com.zhiyi.android_first_demo.model.UnsplashImage
import com.zhiyi.android_first_demo.ui.mangaDetail.MangaDetailActivity
import com.zhiyi.android_first_demo.ui.postDetail.PostDetailActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class ListDataType(val key: String) {
    UnsplashImage("UnsplashImage"),
    MANGA("manga"),
    GAMES("games")
}

// Fragment(R.layout.fragment_base_list)不用写onCreateView了
//
class BaseListFragment : Fragment(R.layout.fragment_base_list) {

    private var dataType: ListDataType = ListDataType.UnsplashImage
    private var _binding: FragmentBaseListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BaseListViewModel by viewModels()
    private lateinit var baseCellAdapter :BaseListCellAdapter

    companion object {
        private const val ARG_DATA_TYPE = "param_data_type"

        @JvmStatic
        fun newInstance(type: ListDataType) =
            BaseListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DATA_TYPE, type.name)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            val typeName = bundle.getString(ARG_DATA_TYPE)
            dataType = try {
                ListDataType.valueOf(typeName ?: ListDataType.UnsplashImage.name)
            } catch (e: IllegalArgumentException) {
                ListDataType.UnsplashImage
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding被系统建好了
        _binding = FragmentBaseListBinding.bind(view)
        baseCellAdapter = BaseListCellAdapter(dataType)

        initUI()

        obViewModel()

        viewModel.requestList(dataType, isRefresh = true)
    }

    fun initUI(){
        // recyclerViewList
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.recyclerViewList.layoutManager = manager

        binding.recyclerViewList.addItemDecoration(
            StaggeredGridSpacingItemDecoration(2, 24)
        )
        binding.recyclerViewList.setBackgroundColor(android.graphics.Color.parseColor("#F5F7F9"))

        binding.recyclerViewList.adapter = baseCellAdapter;

        baseCellAdapter.onItemClickListener = { item ->
           goDetailPage(item)
        }

        addSwipeRefresh()
    }

    fun obViewModel(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dataListState.collectLatest { newList ->
                if (newList.isNotEmpty()) {
                    baseCellAdapter.updateData(newList)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.refreshingState.collect { isRefreshing ->
                if (isRefreshing) {
                    // 只有下拉触发时，这里才会有反应
                } else {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    fun addSwipeRefresh(){
        // 下拉刷新组件
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.requestList(dataType, isRefresh = true)
        }
        // 上拉加载
        binding.recyclerViewList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (viewModel.refreshingState.value) return
                    val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                    val lastVisiblePositions = layoutManager.findLastVisibleItemPositions(null)
                    val lastVisibleItem = lastVisiblePositions.maxOrNull() ?: 0
                    val totalItemCount = layoutManager.itemCount
                    if (lastVisibleItem >= totalItemCount - 1) {
                        viewModel.requestList(dataType,isRefresh = false)
                    }
                }
            }
        })
    }

    fun goDetailPage(item:Any){
        when (dataType) {
            ListDataType.UnsplashImage -> {
                val wallpaperItem = item as? UnsplashImage ?: return // 安全强转
                val intent = Intent(requireContext(), PostDetailActivity::class.java).apply {
                    putExtra("image_data", wallpaperItem) // 传你原本的壁纸对象
                }
                startActivity(intent)
            }

            ListDataType.MANGA -> {
                val mangaItem = item as? MangaItem ?: return // 安全强转

                val intent = Intent(requireContext(), MangaDetailActivity::class.java).apply {
                    // 核心：把当前漫画的唯一 ID 传给详情页即可
                    putExtra("manga_id", mangaItem.malId)
                    // （可选）如果你想让详情页一进去就能立刻展示标题和封面，不用等网络请求，也可以顺便把它们传过去
                    putExtra("manga_title", mangaItem.title)
                    putExtra("manga_cover", mangaItem.images?.jpg?.imageUrl)
                }
                startActivity(intent)
            }
            ListDataType.GAMES -> {
                // val gameItem = item as? GameItem ?: return
                // val intent = Intent(requireContext(), GameDetailActivity::class.java).apply { ... }
                // startActivity(intent)
            }
        }
    }
}