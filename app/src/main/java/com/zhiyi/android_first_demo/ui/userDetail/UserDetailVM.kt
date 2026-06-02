package com.zhiyi.android_first_demo.ui.userDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhiyi.android_first_demo.network.ApiClient
import com.zhiyi.android_first_demo.model.UnsplashUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserDetailVM : ViewModel() {

    private val _userState = MutableStateFlow<UnsplashUser?>(null)
    val userState: StateFlow<UnsplashUser?> = _userState

    fun initUser(user: UnsplashUser) {
        _userState.value = user
    }

    fun requestUserDetail(username:String) {
        viewModelScope.launch {
            try {
                val user = ApiClient.apiService.getUserDetail(username)
                _userState.value = user
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}