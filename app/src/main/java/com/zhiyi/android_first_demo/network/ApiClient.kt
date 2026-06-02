package com.zhiyi.android_first_demo.network

import com.zhiyi.android_first_demo.util.NetworkManager

object ApiClient {

    val apiService: ApiService by lazy {
        // https://jsonplaceholder.typicode.com/
        NetworkManager.createService("https://jsonplaceholder.typicode.com/", ApiService::class.java)
    }


}