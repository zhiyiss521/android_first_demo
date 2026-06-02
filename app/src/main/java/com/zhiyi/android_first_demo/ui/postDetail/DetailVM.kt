package com.zhiyi.android_first_demo.ui.postDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhiyi.android_first_demo.network.ApiClient
import com.zhiyi.android_first_demo.model.UnsplashImage
import com.zhiyi.android_first_demo.model.UnsplashUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailVM : ViewModel() {

    private val _postState = MutableStateFlow<UnsplashImage?>(null)
    val postState: StateFlow<UnsplashImage?> = _postState

    fun initUnsplashImage(image: UnsplashImage) {
        _postState.value = image
    }

    fun requestDetail(id:String) {
        viewModelScope.launch {
            try {
                val posts = ApiClient.apiService.getPhotoDetail(id)
                _postState.value = posts
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
