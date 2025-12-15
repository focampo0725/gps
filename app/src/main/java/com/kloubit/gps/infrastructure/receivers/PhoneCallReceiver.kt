package com.kloubit.gps.infrastructure.receivers

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import com.abx.shared.supportabx.extensions.toast
import com.abx.shared.supportabx.receivers.IHiltReceiver
import com.kloubit.gps.infrastructure.extensions.logerror
import com.kloubit.gps.infrastructure.extensions.logi
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Method
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class PhoneCallReceiver : HiltBroadcastReceiver(), IHiltReceiver {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    var incomingFlag = false
    var telephonyService: com.kloubit.gps.infrastructure.clients.ITelephony? = null


    override fun getActions(): Array<String> = arrayOf(TelephonyManager.ACTION_PHONE_STATE_CHANGED)

    override fun getReceiver(): BroadcastReceiver = this

    companion object {
        var closurePhoneCall: ((isStarted: Boolean) -> Unit)? = null
        var isIncomingCallFlag = false
    }

    var modelCallPhone: Set<String> = listOf("SM-G532M", "").toSet()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val tm = context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            val state = tm.callState
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        }

        if (intent.action!!.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            incomingFlag = false
            isIncomingCallFlag = false
            val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
        } else {
            val tm = context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            try {

                fun isProblemCallPhone(): Boolean = modelCallPhone.contains(Build.MODEL)

                if (isProblemCallPhone()) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        tm.listen(
                            PhoneStateListenerImpl.getInstance(context, ::onIncomingCallStateChanged), PhoneStateListener.LISTEN_CALL_STATE
                        )
                    }, 3000)
                } else {
                    tm.listen(
                        PhoneStateListenerImpl.getInstance(
                            context,
                            ::onIncomingCallStateChanged
                        ), PhoneStateListener.LISTEN_CALL_STATE
                    )
                }

            } catch (e: Exception) {

            }
        }
    }


    fun onIncomingCallStateChanged(state: Int, incomingNumber: String?, context: Context) {

        if (incomingNumber == null)
            context.toast("incoming : $incomingNumber")
        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {
                CallManager.toNotifyEndCall()
            }
            TelephonyManager.CALL_STATE_RINGING -> {
                //Se cambio para el rechazo de todas las llamdas
                checkCall(false, context)

                null

            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                context.toast("CALL_STATE_OFFHOOK")
            }
        }
    }

    var models: Set<String> = listOf("SM-J700M", "").toSet()
    fun isModelNameWithAcceptCallWithoutRoot(): Boolean = models.contains(Build.MODEL)

    fun isRoot(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec("su")
            true
        } catch (e: Exception) {
            false
        } finally {
            if (process != null) {
                try {
                    process.destroy()
                } catch (e: Exception) {
                }
            }
        }
    }


    private fun checkCall(isCall: Boolean, context: Context) {

        try {
            if (!isModelNameWithAcceptCallWithoutRoot() && isRoot()) {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val c = Class.forName(tm.javaClass.name)
                var m: Method? = null
                try {
                    m = c.getDeclaredMethod("getITelephony")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                en caso que no encuentre el mètodo "getITelephony" entonces optamos por el mètodo del CallManager (Android 8.0 o superior.)
//                Target Devices : Samsung A01Core
                if (m == null) {

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startCallWithCallManager(isCall, context)
                        } else
                            logerror("No se concretò la realizaciòn de llamada en ABXGPSPhoneCallReceiver.kt debido a que esta versiòn ${Build.VERSION.SDK_INT} de android està por debajo de lo solicitado ${Build.VERSION_CODES.O}")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        logi("[FrancoTest] (CATCH)No se concretò la realizaciòn de llamada en ABXGPSPhoneCallReceiver.kt debido a que esta versiòn ${Build.VERSION.SDK_INT} de android està por debajo de lo solicitado ${Build.VERSION_CODES.O}")
                        logerror(e.message ?: "exception mientras se intentaba utilizar el mètodo de contestaciòn de llamada #startCallWithCallManager en ABXGPSPhoneCallReceiver.kt"
                        )
                    }
                    return
                }
                fun inputKeyByCodeRoot(keyCode: Int) {
                    logi("FrancoTest - El valor del Keycode:´$keyCode")
                    try {
                        Runtime.getRuntime().exec(
                            arrayOf("/system/bin/su", "-c", "input keyevent $keyCode"
                            )
                        )
                    } catch (e: java.lang.Exception) {
                    } // Version Android 4.2
                    try {
                        Runtime.getRuntime()
                            .exec(arrayOf("su", "-c", "input keyevent $keyCode"))
                    } catch (e: java.lang.Exception) {
                    } // Version Android 4.4
                }

                m.isAccessible = true
                telephonyService = m.invoke(tm) as com.kloubit.gps.infrastructure.clients.ITelephony?
                telephonyService!!.silenceRinger()

                if (isCall) {
                    try {

                        CallManager.toNotifyStartCall()
                        logi("FrancoTest - $isCall - entre al try para iniciarllamada${telephonyService!!.answerRingingCall()}")
                        telephonyService!!.answerRingingCall()  // en algunos modelos samsung (j5 por ejemplo) caemos en error de permisos -->> Neither user 10214 nor current process has android.permission.MODIFY_PHONE_STATE.
                        logi("FrancoTest - entre al try para iniciarllamada${telephonyService!!.answerRingingCall()}")
                    } catch (e: Exception) {
                        CallManager.toNotifyStartCall()
                        inputKeyByCodeRoot(KeyEvent.KEYCODE_CALL)
                    }
                } else {
                    telephonyService!!.endCall()
                    CallManager.toNotifyEndCall()
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startCallWithCallManager(true, context)

                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O) // Oreo = android 8.0
    fun startCallWithCallManager(isCall: Boolean = true, context: Context) {
        logi("FrancoTest - startCallWithCallManager-$isCall")
        if (isCall) CallManager.acceptCall(context)

        else CallManager.endCall(context)
    }

    class CallManager {
        companion object {

            fun toNotifyEndCall() {
                if (isIncomingCallFlag)
                    closurePhoneCall?.invoke(false)
                isIncomingCallFlag = false
            }

            /**
             * notifica por un closure a la app q la llamada ha iniciado
             */
            fun toNotifyStartCall() {
                if (isIncomingCallFlag)
                    closurePhoneCall?.invoke(true)
                isIncomingCallFlag = true
            }

            private fun telecomManager(context: Context): TelecomManager? {
                return context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            }

            @SuppressLint("MissingPermission")
            @RequiresApi(Build.VERSION_CODES.O)
            fun acceptCall(context: Context) {

                toNotifyStartCall()
                logi("FrancoTest-se ejecutetoNor")
                telecomManager(context)?.acceptRingingCall()
            }


            @SuppressLint("MissingPermission", "NewApi")
            @RequiresApi(Build.VERSION_CODES.O)
            fun endCall(context: Context) {
                telecomManager(context)?.endCall()
                toNotifyEndCall()
            }
        }
    }

    class PhoneStateListenerImpl private constructor(
        val context: Context,
        val closure: (Int, String?, Context) -> Unit
    ) : PhoneStateListener() {
        companion object {
            @SuppressLint("StaticFieldLeak")
            @Volatile
            private var instance: PhoneStateListenerImpl? = null

            fun getInstance(context: Context, closure: (Int, String?, Context) -> Unit) =
                instance ?: synchronized(this) {
                    this.logi("[FrancoTest]-LLAMADA!!!! RESPONSE !!!  PASO 2 !! ")
                    instance ?: PhoneStateListenerImpl(context.applicationContext, closure).also {
                        instance = it
                    }
                }
        }

        override fun onCallStateChanged(state: Int, incomingNumber: String?) {
            super.onCallStateChanged(state, incomingNumber)
            incomingNumber?.let {
                closure.invoke(state, it, context.applicationContext)
            }
        }
    }


}
