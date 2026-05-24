package com.zhiyi.android_first_demo.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zhiyi.android_first_demo.R
import com.zhiyi.android_first_demo.viewmodel.DetailVM

class PostDetailActivity : AppCompatActivity() {

    var vm: DetailVM = DetailVM()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        var imageId = intent.getStringExtra("image_id")

        vm.requestDetail(imageId!!)
    }


}