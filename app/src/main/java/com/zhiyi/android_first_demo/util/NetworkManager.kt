package com.zhiyi.android_first_demo.util

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkManager {

    private val okHttpClient: OkHttpClient by lazy {

        val requestInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            val requestBuilder = originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Device-Type", "Android")

            chain.proceed(requestBuilder.url(originalRequest.url).build())
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(requestInterceptor)   // 请求拦截器
            .addInterceptor(loggingInterceptor)  // 挂载日志
            .build()
    }

    fun <T> createService(baseUrl: String, serviceClass: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(serviceClass)
    }
}