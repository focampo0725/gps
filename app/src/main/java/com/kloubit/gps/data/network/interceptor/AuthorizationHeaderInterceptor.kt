package com.kloubit.gps.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationHeaderInterceptor(private val apiKey: String = "", private val cacheDuration: Int = 0) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().newBuilder()
            .build()

        val newRequest = request.newBuilder()
            .url(url)
//            .addHeader("Cache-Control", "public, max-age=$cacheDuration")
//            .addHeader("Authorization", "Bearer $apiKey")
//            .addHeader("Content-Type", "application/json")    // by default
//            .addHeader("Content-Type", "x-www-form-urlencoded")
        if(apiKey.isNotEmpty())
            newRequest.addHeader("Authorization", "Bearer $apiKey")
        if(cacheDuration > 0)
            newRequest.addHeader("Cache-Control", "\"public, max-age=$cacheDuration")
        return chain.proceed(newRequest.build())
    }
}