package com.zhiyi.android_first_demo.ui
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zhiyi.android_first_demo.databinding.ActivityHomeBinding
import com.zhiyi.android_first_demo.util.LogUtil
import com.zhiyi.android_first_demo.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// 当我更换接口的时候，我发现activity完全不用改，只是为了给view绑定东西
// 调用vm的方法，监听vm，赋值给view
class HomeActivity : AppCompatActivity() {
    private var binding: ActivityHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()
    private val postAdapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 这一套都是固定的写法
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate( layoutInflater )
        setContentView(binding!!.root)

        initUI();
        // activity中并没有方法，但是需要调用vm中的方法
        viewModel.requestList()
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
            val intent = Intent(this, PostDetailActivity::class.java).apply {
                putExtra("image_id", item.id)
            }
            startActivity(intent)
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

        // 监听vm
        lifecycleScope.launch {
            viewModel.postListState.collectLatest { newList ->
                // 这里是因为
                // 监听vm中数据的变化，目前和adapter无关
                if (newList.isNotEmpty()) {
                    postAdapter.updateData(newList)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.refreshingState.collect { isRefreshing ->
                if (isRefreshing) {
                    // 只有下拉触发时，这里才会有反应
                } else {
                    binding!!.swipeRefresh.isRefreshing = false
                }
            }
        }


    }


}