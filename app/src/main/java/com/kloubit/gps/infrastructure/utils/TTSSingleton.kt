package com.kloubit.gps.infrastructure.utils

import android.content.Context

object TTSSingleton {
    private var tts: TTS? = null



    fun getTTS(context: Context): TTS {
        if (tts == null) {
            tts = TTS(context.applicationContext)
        }
        return tts!!
    }

    fun close() {
        tts?.close()
        tts = null
    }
}