package com.kloubit.gps.data.network.provider

import android.content.Context
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * provee un cliente http comùn..
 * probado solamente ocn http y los servicios del MPOS hasta el mopmento.. comprobar luego con dominios https ..
 */
class HttpClientProvider(val cache : Cache, val interceptor : Interceptor, val context: Context) : IClientProvider{
    override fun build(): OkHttpClient {
        val cookieJar: ClearableCookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
        return OkHttpClient().newBuilder()
            .cache(cache)
            .cookieJar(cookieJar)
            .readTimeout(30,  TimeUnit.SECONDS)
            .connectTimeout(30,  TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
}