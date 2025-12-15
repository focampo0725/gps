package com.kloubit.gps.domain.utils

import android.content.Context
import com.abx.shared.supportabx.extensions.doAsync
import com.kloubit.gps.data.common.Actions
import com.kloubit.gps.di.qualifiers.NotUIThread
import com.kloubit.gps.domain.service.actionOnAppService
import java.lang.Exception

/**
 * reinicia servicio en casos como :
 * - BootReceiver
 * - updateMobileConfiguration
 * - updateTrackSetting
 */
@NotUIThread
fun restartServiceAsync(context: Context){
    doAsync{
        try {
            actionOnAppService(Actions.STOP, context)
            Thread.sleep(500)
            actionOnAppService(Actions.START, context)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}

