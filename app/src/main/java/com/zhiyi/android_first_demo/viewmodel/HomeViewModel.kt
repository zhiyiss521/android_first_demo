package com.zhiyi.android_first_demo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhiyi.android_first_demo.api.ApiClient
import com.zhiyi.android_first_demo.model.UnsplashImage
import com.zhiyi.android_first_demo.util.LogUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// vm中没有view，只有data和改变data的函数，
class HomeViewModel : ViewModel() {

    // 理解为固定套路好了，就是一个List<Post>的包装类
    private val _postListState = MutableStateFlow<List<UnsplashImage>>(emptyList())
    val postListState: StateFlow<List<UnsplashImage>> = _postListState

    // 控制是否加载中的状态
    private val _refreshingState = MutableStateFlow(false)
    val refreshingState = _refreshingState.asStateFlow()

    private var currentPage = 1
    private val allPostsList = mutableListOf<UnsplashImage>()

    fun requestList(isRefresh: Boolean = true) {
        if(isRefresh){
            currentPage = 1
        }else{
            currentPage++
        }

        viewModelScope.launch {
            _refreshingState.value = true
            try {
                val posts = ApiClient.apiService.getPhotos(currentPage)
                if(isRefresh){
                    allPostsList.clear()
                }
                allPostsList.addAll(posts)
                _postListState.value = ArrayList(allPostsList)
            } catch (e: Exception) {
                e.message?.let { LogUtil.d(it) };
                if (!isRefresh && currentPage > 1) {
                    currentPage--
                }
                e.printStackTrace()
            }finally {
                LogUtil.d("vm中刷新状态改变了")
                _refreshingState.value = false
            }
        }
    }


}