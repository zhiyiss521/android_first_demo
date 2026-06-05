package com.zhiyi.android_first_demo.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zhiyi.android_first_demo.databinding.FragmentHomeBinding
import com.zhiyi.android_first_demo.model.UnsplashImage
import com.zhiyi.android_first_demo.ui.postDetail.PostDetailActivity
import com.zhiyi.android_first_demo.util.LogUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// 当我更换接口的时候，我发现activity完全不用改，只是为了给view绑定东西
// 调用vm的方法，监听vm，赋值给view
class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()
    private val postAdapter = PostAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate( layoutInflater )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        LogUtil.d("homefragment onviewCreated")
//        viewModel.requestList()
        viewModel.requestNews()
    }

    fun initUI(){
        // recyclerViewList
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding!!.recyclerViewList.layoutManager = manager

        val spacingInPixels = 24
        binding!!.recyclerViewList.addItemDecoration(
            StaggeredGridSpacingItemDecoration(2, spacingInPixels)
        )
        binding!!.recyclerViewList.setBackgroundColor(android.graphics.Color.parseColor("#F5F7F9"))


        binding!!.recyclerViewList.adapter = postAdapter;

        postAdapter.onItemClickListener = { item ->
           goDetail(item)
        }

        // 下拉加载组件
        binding!!.swipeRefresh.setOnRefreshListener {
            viewModel.requestList()
        }
        // 上拉加载
        binding!!.recyclerViewList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (viewModel.refreshingState.value) return
                    val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                    val lastVisiblePositions = layoutManager.findLastVisibleItemPositions(null)
                    val lastVisibleItem = lastVisiblePositions.maxOrNull() ?: 0
                    val totalItemCount = layoutManager.itemCount
                    if (lastVisibleItem >= totalItemCount - 1) {
                        viewModel.requestList(isRefresh = false)
                    }
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postListState.collectLatest { newList ->
                if (newList.isNotEmpty()) {
                    showContent()
                    postAdapter.updateData(newList)
                }else{
                    showEmpty()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.refreshingState.collect { isRefreshing ->
                if (isRefreshing) {
                    // 只有下拉触发时，这里才会有反应
                } else {
                    binding!!.swipeRefresh.isRefreshing = false
                }
            }
        }

    }

    fun goDetail(item:UnsplashImage){
        val intent = Intent(requireContext(), PostDetailActivity::class.java).apply {
            putExtra("image_data", item)
        }
        startActivity(intent)
    }

    private fun showContent() {
        LogUtil.d("showContent()")
        binding?.progressBar?.visibility = View.GONE
        binding?.layoutEmpty?.visibility = View.GONE
        binding?.swipeRefresh?.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        LogUtil.d("showEmpty()")

        binding?.progressBar?.visibility = View.GONE
        binding?.layoutEmpty?.visibility = View.VISIBLE
        binding?.swipeRefresh?.visibility = View.GONE
    }

}