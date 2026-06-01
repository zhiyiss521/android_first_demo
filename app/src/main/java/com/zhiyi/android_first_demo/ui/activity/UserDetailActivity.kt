package com.zhiyi.android_first_demo.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.appbar.AppBarLayout
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
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)
        binding.lifecycleOwner = this
        binding.vm = vm

        initUI()
        getData()

    }

    fun initUI(){
        setAppBar()

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

    fun setAppBar(){
        binding.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                val total = appBarLayout.totalScrollRange

                val percent = kotlin.math.abs(verticalOffset) / total.toFloat()

                if (percent > 0.75f) {

                    // 吸顶状态
                    binding.layoutCollapsedUser.animate()
                        .alpha(1f)
                        .setDuration(180)
                        .start()

                    binding.layoutCollapsedUser.visibility = View.VISIBLE

                    binding.cardBack.setCardBackgroundColor(Color.WHITE)

                    binding.cardBack.cardElevation = 6f

                    binding.btnBack.imageTintList =
                        ColorStateList.valueOf(Color.BLACK)

                } else {

                    // 展开状态
                    binding.layoutCollapsedUser.animate()
                        .alpha(0f)
                        .setDuration(180)
                        .start()

                    binding.layoutCollapsedUser.visibility = View.INVISIBLE

                    binding.cardBack.setCardBackgroundColor(
                        Color.parseColor("#66000000")
                    )

                    binding.cardBack.cardElevation = 0f

                    binding.btnBack.imageTintList =
                        ColorStateList.valueOf(Color.WHITE)
                }
            }
        )
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