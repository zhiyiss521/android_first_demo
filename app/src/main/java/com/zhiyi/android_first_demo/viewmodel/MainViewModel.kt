package com.zhiyi.android_first_demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhiyi.android_first_demo.api.ApiClient
import com.zhiyi.android_first_demo.model.ImagePost
import com.zhiyi.android_first_demo.model.Post
import com.zhiyi.android_first_demo.model.UnsplashImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// vm中没有view，只有data和改变data的函数，
class MainViewModel : ViewModel() {

    // 理解为固定套路好了，就是一个List<Post>的包装类
    private val _postListState = MutableStateFlow<List<UnsplashImage>>(emptyList())
    val postListState: StateFlow<List<UnsplashImage>> = _postListState

    fun requestList() {
        viewModelScope.launch {
            try {
                val posts = ApiClient.apiService.getPhotos()
                _postListState.value = posts
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}