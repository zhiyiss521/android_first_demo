package com.zhiyi.android_first_demo.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zhiyi.android_first_demo.R
import com.zhiyi.android_first_demo.databinding.ActivityPostDetailBinding
import com.zhiyi.android_first_demo.viewmodel.DetailVM

class PostDetailActivity : AppCompatActivity() {

    val vm: DetailVM by viewModels<DetailVM>()
    lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
        binding.lifecycleOwner = this
        binding.vm = vm

        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        var imageId = intent.getStringExtra("image_id")
        vm.requestDetail(imageId!!)
    }




}