package com.zhiyi.android_first_demo.ui.baseList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhiyi.android_first_demo.network.ApiClient
import com.zhiyi.android_first_demo.model.UnsplashImage
import com.zhiyi.android_first_demo.util.LogUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// vm中没有view，只有data和改变data的函数，
class BaseListViewModel : ViewModel() {

    // 理解为固定套路好了，就是一个List<Post>的包装类
    private val _dataListState = MutableStateFlow<List<Any>>(emptyList())
    val dataListState: StateFlow<List<Any>> = _dataListState

    // 控制是否加载中的状态
    private val _refreshingState = MutableStateFlow(false)
    val refreshingState = _refreshingState.asStateFlow()

    private var currentPage = 1
    private val allPostsList = mutableListOf<Any>()

    fun requestList(dataType: ListDataType,isRefresh: Boolean = true) {
        if(isRefresh){
            currentPage = 1
        }else{
            currentPage++
        }

        viewModelScope.launch {
            _refreshingState.value = true
            try {
                var data: List<Any> =  emptyList()
                if(dataType == ListDataType.UnsplashImage){
                   data = ApiClient.unsplashService.getPhotos(currentPage)
                }else if(dataType == ListDataType.MANGA){
                    var ret = ApiClient.mangaService.getTopManga(currentPage)
                   data = ret.data
                }else if(dataType == ListDataType.GAMES){

                }

                if(isRefresh){
                    allPostsList.clear()
                }
                allPostsList.addAll(data)
                _dataListState.value = ArrayList(allPostsList)
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