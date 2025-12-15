package com.kloubit.gps.data.network.provider

import okhttp3.OkHttpClient

interface IClientProvider {
    fun build () : OkHttpClient
}