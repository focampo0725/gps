package com.kloubit.gps.infrastructure.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission

import com.abx.shared.supportabx.extensions.logi
import com.abx.shared.supportabx.extensions.toast
import com.abx.shared.supportabx.receivers.IHiltReceiver
import com.kloubit.gps.BuildConfig
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.infrastructure.stateful.AppState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * no usar comandos con guìon bajo porque
 * en ocasiones es reemplazado por un espacio en blanco..
 */
@AndroidEntryPoint
class SMSReceiver : HiltBroadcastReceiver(), IHiltReceiver {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var appState: AppState

    @Inject
    lateinit var appRepository: AppRepository

    val COMMAND_OPEN_VIRTUAL_BUTTON = "OBV"
    val COMMAND_CLOSE_VIRTUAL_BUTTON = "CBV"
    val COMMAND_REBOOT_BUTTON = "BR"
    val COMMAND_DATA_MESSAGE = "DM"

    override fun getActions(): Array<String> = arrayOf("android.provider.Telephony.SMS_RECEIVED")
    override fun getReceiver(): BroadcastReceiver = this

    @SuppressLint("WrongConstant")
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        intent?.extras?.let {
            val pdus = it["pdus"] as Array<*>? ?: return
            pdus.first().let {
                val smsMessage = SmsMessage.createFromPdu(it as ByteArray)
                val message = smsMessage.displayMessageBody
                val senderNumber = smsMessage.originatingAddress

                if (BuildConfig.DEBUG)
                    context.toast("Message received : $message")

                when (message.toUpperCase()) {
                    COMMAND_OPEN_VIRTUAL_BUTTON -> {

                    }
                    COMMAND_CLOSE_VIRTUAL_BUTTON -> {

                    }
                    COMMAND_REBOOT_BUTTON -> {
                        context.logi("Entre aqui")
                    }
                    COMMAND_DATA_MESSAGE -> {
                        checkMobileInternet { isExistInternet ->
                            try {
                                if ((context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY) ||
                                            context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)) &&
                                    isExistInternet
                                ) {
                                    val strMessage = buildMessage(context, isExistInternet)
                                    val smsManager = SmsManager.getDefault()
                                    if (strMessage.length < 160)
                                        smsManager.sendTextMessage(senderNumber, null, strMessage, null, null)
                                    else {
                                        val messageParts = smsManager.divideMessage(strMessage)
                                        smsManager.sendMultipartTextMessage(senderNumber, null, messageParts, null, null)
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun checkMobileInternet(onInternetCallback: (Boolean) -> Unit) {
        val timeoutMs = 1500
        val socket = Socket()
        val sockaddr = InetSocketAddress("8.8.8.8", 53)

        Thread {
            try {
                socket.connect(sockaddr, timeoutMs)
                socket.close()
                onInternetCallback(true)
            } catch (e: IOException) {
                onInternetCallback(false)
            }
        }.start()
    }

    @SuppressLint("MissingPermission")
    private fun buildMessage(context: Context, isExistInternet: Boolean): String {
        return buildString {
            append("DM: ${getMobileDataUsage(context)} \n")
            append("ES: ${if (isNetworkAvailable(context)) "Ok" else "Error"}\n")
            append("PIN: ${if (isExistInternet) "Ok" else "Error"}\n")
//            append("APN: ${getSelectedApn(context)}\n")
            append("LatLng: ${appState.location?.latitude ?: 0.0F},${appState.location?.longitude ?: 0.0F}")
        }
    }


    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    fun getMobileDataUsage(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (telephonyManager.simState != TelephonyManager.SIM_STATE_READY) {
            return "0.00 MB"
        }

        val mobileRxBytes = TrafficStats.getMobileRxBytes().toDouble()
        val mobileTxBytes = TrafficStats.getMobileTxBytes().toDouble()

        val totalMobileBytes = mobileRxBytes + mobileTxBytes
        val totalMobileMB = totalMobileBytes / (1024.0 * 1024.0)

        val decimalFormat = DecimalFormat("#.00")
        return decimalFormat.format(totalMobileMB) + " MB"
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }






}

