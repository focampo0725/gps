package com.kloubit.gps.infrastructure.clients

import android.content.Context
interface IClient {
    fun onStart(context: Context)
    fun onDestroy()
}