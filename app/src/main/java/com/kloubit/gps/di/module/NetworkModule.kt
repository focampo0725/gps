package com.kloubit.gps.di.module

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.kloubit.gps.BuildConfig
import com.kloubit.gps.data.network.CompanyListAPI
import com.kloubit.gps.data.network.interceptor.AuthorizationHeaderInterceptor
import com.kloubit.gps.data.network.provider.HttpClientProvider
import com.kloubit.gps.di.qualifiers.CacheDurationQualifier
import com.kloubit.gps.di.qualifiers.ListCompanyQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule @Inject constructor(){

    @Provides @Singleton
    fun provideCache(@ApplicationContext context: Context) : Cache = Cache(context.cacheDir, 10 * 1024 * 1024.toLong())

    @Provides @Singleton
    fun provideApiKey(): String = "something"

    @CacheDurationQualifier
    @Provides @Singleton
    fun provideCacheDuration(@ApplicationContext context: Context) : Int = 86400

    //////////////////////COMPANY/////////////////////////////////////
    @Provides @Singleton @ListCompanyQualifier
    fun provideHttpClientListCompany(
        @ApplicationContext context: Context, cache: Cache,
        @CacheDurationQualifier cacheDuration: Int,
    ) : OkHttpClient =
        HttpClientProvider(cache = cache, interceptor = AuthorizationHeaderInterceptor(), context = context).build()

    @Provides @Singleton @ListCompanyQualifier
    fun providesListCompanyApiRetrofit(@ListCompanyQualifier client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("BuildConfig.COMPANIES_HOSTNAME")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
    @Provides @Singleton
    fun providesListCompanyApi(@ListCompanyQualifier retrofit: Retrofit): CompanyListAPI = retrofit.create(CompanyListAPI::class.java)

}