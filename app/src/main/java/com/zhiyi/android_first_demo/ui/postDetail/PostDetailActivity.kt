package com.zhiyi.android_first_demo.ui.postDetail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zhiyi.android_first_demo.R
import com.zhiyi.android_first_demo.databinding.ActivityPostDetailBinding
import com.zhiyi.android_first_demo.model.UnsplashImage
import com.zhiyi.android_first_demo.ui.userDetail.UserDetailActivity
import com.zhiyi.android_first_demo.util.LogUtil
import kotlinx.coroutines.launch
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.annotations.IconFactory

class PostDetailActivity : AppCompatActivity() {

    val vm: DetailVM by viewModels<DetailVM>()
    lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
        binding.lifecycleOwner = this
        binding.vm = vm

        initUI()
        getData()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    fun initUI(){
        binding.detailToolbar.setNavigationOnClickListener {
            finish()
        }

        binding.ivUserAvatar.setOnClickListener{
            val model = vm.postState.value
            if(model?.user != null){
                val intent = Intent(this@PostDetailActivity, UserDetailActivity::class.java).apply {
                    val user = model.user
                    LogUtil.d("🚀${user.username}")
                    putExtra("user_data", user)
                }
                startActivity(intent)
            }
        }

        binding.mapView.getMapAsync { map ->
            map.setStyle("https://api.maptiler.com/maps/streets-v2/style.json?key=x0ic7MgzoJPpWBnKjv4D"){ style ->

            }
            map.cameraPosition = CameraPosition.Builder()
                .target(LatLng(0.0,0.0))
                .zoom(1.0)
                .build()
        }

        lifecycleScope.launch {
            vm.postState.collect { unsplashImage ->
                binding.mapView.getMapAsync { map ->
                    loadLocationData(unsplashImage,map)
                }
            }
        }
    }

    fun getData(){
        val data = intent.getParcelableExtra<UnsplashImage>("image_data")
        if (data != null) {
            vm.initUnsplashImage(data)
            vm.requestDetail(data.id)
        }
    }

    fun loadLocationData(unsplashImage:UnsplashImage?, map:MapLibreMap){
        val lat = unsplashImage?.location?.position?.latitude.takeIf { it != 0.0 }
        val lng = unsplashImage?.location?.position?.longitude.takeIf { it != 0.0 }
        LogUtil.d("🚀lat:${lat},lng:${lng}")
        val DEFAULT_LOCATION = LatLng(40.6892494, -74.0445004)
        val targetLocation = if (lat != null && lng != null) {
            LatLng(lat, lng)
        } else {
            DEFAULT_LOCATION
        }

        map.clear()
        map.animateCamera( CameraUpdateFactory.newLatLngZoom(targetLocation,16.0), 2000 )
        val iconFactory = IconFactory.getInstance(this)

        Glide.with(this)
            .asBitmap()
            .load(unsplashImage?.urls?.thumb ?: "")
            .circleCrop()
            .override(120, 120)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    map.clear()
                    map.addMarker(
                        MarkerOptions()
                            .position(targetLocation)
                            .icon(iconFactory.fromBitmap(resource))
                    )
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    map.clear()
                    map.addMarker(MarkerOptions().position(targetLocation))
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

    }
}