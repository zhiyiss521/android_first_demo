package com.zhiyi.android_first_demo.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.zhiyi.android_first_demo.R
import com.zhiyi.android_first_demo.databinding.ActivityPostDetailBinding
import com.zhiyi.android_first_demo.util.LogUtil
import com.zhiyi.android_first_demo.viewmodel.DetailVM
import kotlinx.coroutines.launch
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

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
                if (unsplashImage?.location?.position != null) {
                    val lat = unsplashImage?.location?.position.latitude
                    val lng = unsplashImage?.location?.position.longitude
                    LogUtil.d("lat:${lat},lng:${lng},name:${unsplashImage?.location?.country}")
                    val targetLocation = LatLng(lat,lng)

                    binding.mapView.getMapAsync { map ->
                        map.animateCamera( CameraUpdateFactory.newLatLngZoom(targetLocation,16.0), 2000 )



                    }
                }
            }
        }
    }

    fun getData(){
        var imageId = intent.getStringExtra("image_id")
        vm.requestDetail(imageId!!)
    }



}