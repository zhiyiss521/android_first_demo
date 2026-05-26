package com.zhiyi.android_first_demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhiyi.android_first_demo.api.ApiClient
import com.zhiyi.android_first_demo.model.ImagePost
import com.zhiyi.android_first_demo.model.UnsplashImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailVM : ViewModel() {

    private val _postState = MutableStateFlow<UnsplashImage?>(null)
    val postState: StateFlow<UnsplashImage?> = _postState

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
