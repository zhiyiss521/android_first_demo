package com.zhiyi.android_first_demo.ui
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zhiyi.android_first_demo.databinding.ActivityHomeBinding
import com.zhiyi.android_first_demo.util.LogUtil
import com.zhiyi.android_first_demo.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// 当我更换接口的时候，我发现activity完全不用改，只是为了给view绑定东西
// 调用vm的方法，监听vm，赋值给view
class HomeActivity : AppCompatActivity() {
    private var binding: ActivityHomeBinding? = null
    private val viewModel: MainViewModel by viewModels()
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
        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding!!.recyclerViewList.layoutManager = manager

        val spacingInPixels = 24
        binding!!.recyclerViewList.addItemDecoration(
            StaggeredGridSpacingItemDecoration(2, spacingInPixels)
        )
        binding!!.recyclerViewList.setBackgroundColor(android.graphics.Color.parseColor("#F5F7F9"))


        binding!!.recyclerViewList.adapter = postAdapter;

        lifecycleScope.launch {
            viewModel.postListState.collectLatest { newList ->
                binding!!.swipeRefresh.isRefreshing = false
                // 监听vm中数据的变化，目前和adapter无关
                if (newList.isNotEmpty()) {
                    postAdapter.dataList = newList
                }
            }
        }
        postAdapter.onItemClickListener = { item ->
            val intent = Intent(this, PostDetailActivity::class.java).apply {
                putExtra("image_id", item.id)
            }
            startActivity(intent)
        }

        binding!!.swipeRefresh.setOnRefreshListener {
            viewModel.requestList()
        }

    }


}