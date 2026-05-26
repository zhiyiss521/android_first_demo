package com.zhiyi.android_first_demo.util

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

// Retrofit,底层是okHttpClient
object NetworkManager {

    private val okHttpClient: OkHttpClient by lazy {

        val requestInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            val requestBuilder = originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","Client-ID -sncfr6j20Nw3vWh5vY2JSAti-AS-X3d1OKoHk9pgJo")

            chain.proceed(requestBuilder.url(originalRequest.url).build())
        }

        var currentUrl = ""
        var currentStatus = ""
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                when {
                    message.startsWith("--> GET") || message.startsWith("--> POST") -> {
                        currentUrl = message.substringAfter("--> GET ").substringAfter("--> POST ")
                    }
                    message.startsWith("<-- ") && !message.contains("END HTTP") -> {
                        currentStatus = message.substringAfter("<-- ").substringBefore(" ")
                    }
                    message.startsWith("{") || message.startsWith("[") -> {
                        val prettyJson = try {
                            if (message.startsWith("{")) {
                                JSONObject(message).toString(4)
                            } else {
                                JSONArray(message).toString(4)
                            }
                        } catch (e: Exception) {
                            message
                        }
                        val finalLog = "[URL]: $currentUrl\n[status]: $currentStatus\n[response]:\n$prettyJson".trimIndent()
                        LogUtil.d(finalLog)
                    }
                    message.startsWith("<-- HTTP FAILED") -> {
                        LogUtil.d("[URL]: $currentUrl [status]: FAILED  [response]: ${message.substringAfter("FAILED: ")}")
                    }
                }
            }
        }).apply {
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