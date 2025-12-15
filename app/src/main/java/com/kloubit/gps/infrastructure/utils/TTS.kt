package com.kloubit.gps.infrastructure.utils

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.*

class TTS(context: Context) : UtteranceProgressListener(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech = TextToSpeech(context, this)
    private var tipoReproduccion: Int = 0
//    private lateinit var context :Context

    @SuppressLint("StaticFieldLeak")
    object Comun {
        var appContext: Context? = null

        fun getContext(): Context {
            return appContext!!
        }
    }

    init {
        elevarVol(context)
        tts.setOnUtteranceProgressListener(this)
    }

    override fun onInit(estado: Int) {
        if (estado == TextToSpeech.SUCCESS) {
            val loc = Locale("es", "ES")
            val result = tts.setLanguage(loc)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){

            }else{}
        }else{
            Log.d("TTSManager", "manejar envento de en caso de se logre la inicializacion $estado")

        }

    }

    private fun reproduccionDinamica(texto: String, tipoReproduccion: Int) {
        val myHashAlarm = HashMap<String, String>()
        myHashAlarm[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = AudioManager.STREAM_MUSIC.toString()
        this.tipoReproduccion = tipoReproduccion
        if (tipoReproduccion == TIPOREPRODUCCION.VOZPARLANTEGRANDE.getValue()) {
            HidBridge.getInstance(Comun.getContext()).WriteData(offParlantePequenio.toByteArray())
            HidBridge.getInstance(Comun.getContext()).WriteData(onParlanteGrande.toByteArray())
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, myHashAlarm)
        } else if (tipoReproduccion == TIPOREPRODUCCION.VOZPARLANTEPEQUENIO.getValue()) {
            HidBridge.getInstance(Comun.getContext()).WriteData(offParlanteGrande.toByteArray())
            HidBridge.getInstance(Comun.getContext()).WriteData(onParlantePequenio.toByteArray())
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, myHashAlarm)
        }
    }

    override fun onStart(utteranceId: String?) {}

    override fun onDone(utteranceId: String?) {
        if (tipoReproduccion == TIPOREPRODUCCION.VOZPARLANTEGRANDE.getValue()) {

            HidBridge.getInstance(Comun.getContext()).WriteData(offParlanteGrande.toByteArray())
        } else if (tipoReproduccion == TIPOREPRODUCCION.VOZPARLANTEPEQUENIO.getValue()) {
            HidBridge.getInstance(Comun.getContext()).WriteData(offParlantePequenio.toByteArray())
        }
    }

    override fun onError(utteranceId: String?) {}

    private fun reproducir(texto: String, isLento: Boolean) {
        val params = Bundle()
        params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, params, null)
        val VELOCREPRODUCCION = 0.9f
        if (isLento)
            tts.setSpeechRate(VELOCREPRODUCCION)
    }

    private fun mostrar(texto: String, context: Context) {

    }

    fun accionMensaje(texto: String, tiporeproduccion: TIPOREPRODUCCION, context: Context) {
        when (tiporeproduccion) {
            TIPOREPRODUCCION.VOZ -> reproducir(texto, false)
            TIPOREPRODUCCION.TOAST -> mostrar(texto, context)
            TIPOREPRODUCCION.VOZLENTA -> reproducir(texto, true)
            else -> {}
        }
    }

    fun accionMensaje(texto: String, codTipoReproduccion: Int, context: Context) {
        val eTipoReproduccion = getETipoReproduccion(codTipoReproduccion)
        when (eTipoReproduccion) {
            TIPOREPRODUCCION.VOZ -> reproducir(texto, false)
            TIPOREPRODUCCION.TOAST -> mostrar(texto, context)
            TIPOREPRODUCCION.VOZLENTA -> reproducir(texto, true)
            else -> {}
        }
    }

    fun accionMensajeSyncronized(texto: String, codTipoReproduccion: Int, context: Context) {
        val eTipoReproduccion = getETipoReproduccion(codTipoReproduccion)
        if (tts.isSpeaking() && codTipoReproduccion == TIPOREPRODUCCION.VOZ.getValue())
            tts.stop()
        when (eTipoReproduccion) {
            TIPOREPRODUCCION.VOZ -> reproducir(texto, false)
            TIPOREPRODUCCION.TOAST -> mostrar(texto, context)
            TIPOREPRODUCCION.VOZLENTA -> reproducir(texto, true)
            TIPOREPRODUCCION.VOZPARLANTEGRANDE -> reproduccionDinamica(texto, codTipoReproduccion)
            TIPOREPRODUCCION.VOZPARLANTEPEQUENIO -> reproduccionDinamica(texto, codTipoReproduccion)
            else -> {}
        }
    }

    private fun getETipoReproduccion(codTipoReproduccion: Int): TIPOREPRODUCCION {
        return when (codTipoReproduccion) {
            TIPOREPRODUCCION.TOAST.getValue() -> TIPOREPRODUCCION.TOAST
            TIPOREPRODUCCION.VOZ.getValue() -> TIPOREPRODUCCION.VOZ
            TIPOREPRODUCCION.VOZPARLANTEGRANDE.getValue() -> TIPOREPRODUCCION.VOZPARLANTEGRANDE
            TIPOREPRODUCCION.VOZPARLANTEPEQUENIO.getValue() -> TIPOREPRODUCCION.VOZPARLANTEPEQUENIO
            TIPOREPRODUCCION.VOZLENTA.getValue() -> TIPOREPRODUCCION.VOZLENTA
            else -> TIPOREPRODUCCION.SINACCION
        }
    }

    fun elevarVol(context: Context) {
        if (Build.MODEL == "SM-G532M") {
            try {
                val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val numVolActual = am.getStreamVolume(AudioManager.STREAM_MUSIC)
                val numVolMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                if (NUMINTENTOSUBIRVOLUMEN > 15)
                    return
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
                am.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL, am.getStreamMaxVolume(
                        AudioManager.STREAM_VOICE_CALL), 0)
                am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0)
                am.setStreamVolume(
                    AudioManager.STREAM_NOTIFICATION, am.getStreamMaxVolume(
                        AudioManager.STREAM_NOTIFICATION), 0)
                am.setStreamVolume(AudioManager.STREAM_ALARM, am.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0)
                NUMINTENTOSUBIRVOLUMEN += 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            try {
                val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val numVolActual = am.getStreamVolume(AudioManager.STREAM_MUSIC)
                val numVolMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                if (numVolActual == numVolMax) {
                    ISELEVARVOLUMEN = false
                    return
                }
                am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
                am.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL, am.getStreamMaxVolume(
                        AudioManager.STREAM_VOICE_CALL), 0)
                am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0)
                am.setStreamVolume(
                    AudioManager.STREAM_NOTIFICATION, am.getStreamMaxVolume(
                        AudioManager.STREAM_NOTIFICATION), 0)
                am.setStreamVolume(AudioManager.STREAM_ALARM, am.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0)
                NUMINTENTOSUBIRVOLUMEN += 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private var NUMINTENTOSUBIRVOLUMEN = 0
        private var ISELEVARVOLUMEN = true

        const val onParlanteGrande = "A"
        const val offParlanteGrande = "B"
        const val onParlantePequenio = "C"
        const val offParlantePequenio = "D"
    }


    enum class TIPOREPRODUCCION(val n: Int) {
        SINACCION(0),
        TOAST(1),
        VOZ(2),
        VOZLENTA(3),
        VOZPARLANTEGRANDE(4),
        VOZPARLANTEPEQUENIO(5);

        fun getValue(): Int {
            return n
        }
    }

    fun close() {
        tts.stop()
        tts.shutdown()
    }
}