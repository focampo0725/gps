package com.kloubit.gps.domain.service

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build

import com.abx.shared.supportabx.extensions.logi
import com.kloubit.gps.data.common.Actions
import com.kloubit.gps.data.common.AppService

enum class ServiceState {
    STARTED,
    STOPPED,
}

private const val name = "SPYSERVICE_KEY"
private const val key = "SPYSERVICE_STATE"

fun setServiceState(context: Context, state: ServiceState) {
    val sharedPrefs = getPreferences(context)
    sharedPrefs.edit().let {
        it.putString(key, state.name)
        it.apply()
    }
}

fun getServiceState(context: Context): ServiceState {
    val sharedPrefs = getPreferences(context)
    val value = sharedPrefs.getString(key, ServiceState.STOPPED.name)
    return ServiceState.valueOf(value!!)
}

private fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(name, 0)
}



fun actionOnAppService(action: Actions, context: Context) {
    if (getServiceState(context) == ServiceState.STOPPED && action == Actions.STOP) return
    Intent(context, AppService::class.java).also {
        it.action = action.name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.logi("Starting the service in >=26 Mode")
            context.startForegroundService(it)
            return
        }
        context.logi("Starting the service in < 26 Mode")
        context.startService(it)
    }
}
