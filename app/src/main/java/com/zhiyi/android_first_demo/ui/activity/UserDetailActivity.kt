package com.zhiyi.android_first_demo.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zhiyi.android_first_demo.R
import com.zhiyi.android_first_demo.databinding.ActivityPostDetailBinding
import com.zhiyi.android_first_demo.databinding.ActivityUserDetailBinding
import com.zhiyi.android_first_demo.model.UnsplashUser
import com.zhiyi.android_first_demo.ui.adapter.PhotoAdapter
import com.zhiyi.android_first_demo.util.LogUtil
import com.zhiyi.android_first_demo.viewmodel.DetailVM
import com.zhiyi.android_first_demo.viewmodel.UserDetailVM
import kotlinx.coroutines.launch
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng

class UserDetailActivity : AppCompatActivity() {

    val vm: UserDetailVM by viewModels<UserDetailVM>()
    lateinit var binding: ActivityUserDetailBinding
    private val adapter = PhotoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)
        binding.lifecycleOwner = this
        binding.vm = vm

        initUI()
        getData()

    }

    fun initUI(){

        binding.btnBack.setOnClickListener( {
            finish()
        })

        binding.rvPhotos.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        binding.rvPhotos.adapter = adapter


        lifecycleScope.launch {
            vm.userState.collect { user ->
                adapter.submitList(user?.photos)
            }
        }
    }

    fun getData(){
        val user = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user_data", UnsplashUser::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("user_data")
        }

        LogUtil.d("🚀${user?.username}")
        vm.initUser(user!!)

        vm.requestUserDetail(user.username!!)

    }
}