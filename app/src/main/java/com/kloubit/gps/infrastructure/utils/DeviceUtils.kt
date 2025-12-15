package com.kloubit.gps.infrastructure.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.os.BatteryManager
import com.abx.shared.supportabx.extensions.logerror
import com.abx.shared.supportabx.extensions.logi
import com.abx.shared.supportabx.intents.RequestIntent
import com.abx.shared.supportabx.receivers.SupportSuiteReceiver.Companion.closure
import com.google.gson.Gson
import com.kloubit.gps.di.qualifiers.NotUIThread
import com.kloubit.gps.domain.dto.DeviceDataDTO
import io.reactivex.Single
import io.reactivex.SingleEmitter


class DeviceUtils(){
    companion object{

        private val BACKBUTTON_PACKAGE_NAME = "nu.back.button"
        private val RECEIVER_SUSCRIPTION_DELAY = 1000L

        /**
         * return the battery level
         */
        fun getBatteryLevel(context: Context): Int {
            try {
                val batIntentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                val battery: Intent? = context.registerReceiver(null, batIntentFilter)
                if (battery != null) return battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            } catch (e: Exception) {
                context.logerror(e.message ?: "")
            }
            return -1
        }

        fun getAppVersion(context: Context) : String{
            var pInfo: PackageInfo? = null
            try {
                pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                if (pInfo == null) return ""
                return pInfo.versionName + ""
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun getAppVersionByApps(context: Context, packagesWithVersion : List<String>) : HashMap<String, String>{
            val versionsMap : HashMap<String, String> = hashMapOf()
            try {
                val packageList : List<PackageInfo> = context.packageManager.getInstalledPackages(0)
                packagesWithVersion.forEach { matchPackage ->
                    packageList.filter { it.packageName.contains(matchPackage) }.firstOrNull()?.let {
                        versionsMap.put(it.packageName, it.versionName)
                    }
                }
                return versionsMap
            } catch (e: java.lang.Exception) {
                context.logerror(e.message ?: "exception in DeviceUtils#getAppVersionByApps")
            }
            return versionsMap
        }
//
//
        /**
         * flujo reactivo que se encarga de verificar la comunicaciòn entre la libreria de accesibilidad y el appservice
         */
        @NotUIThread
//        fun checkCommunicationBetweenLibAccessibility(context: Context, timeOut : Long = 5000) : Single<Boolean> {
//            return Single.create { emitter: SingleEmitter<Boolean> ->
//                RequestIntent.requestHearbeat(context) { s ->
//                    context.logi("while - loop enabled and answersssss intent *****")
//                    emitter.onSuccess(true)
//                }
//            }.timeout(timeOut, java.util.concurrent.TimeUnit.MILLISECONDS)
//        }

//        fun checkCommunicationBetweenLibAccessibility(context: Context, timeOut : Long = 5000) : Single<Boolean> {
//            return Single.defer {
//                Single.create { emitter: SingleEmitter<Boolean> ->
//                    RequestIntent.requestHearbeat(context) { s ->
//
//                        context.logi("while - loop enabled and answersssss intent *****")
//                        emitter.onSuccess(true)
//                    }
//                    context.logi("After calling requestHearbeat")
//                }
//            }.timeout(timeOut, java.util.concurrent.TimeUnit.MILLISECONDS)
//        }

        fun checkCommunicationBetweenLibAccessibility(context: Context, timeOut: Long = 5000): Single<Boolean> {
            context.logi("[SPSUITE-TRACE] RetryAttempt checkCommunicationBetweenLibAccessibility 01")
            return Single.defer {
                context.logi("[SPSUITE-TRACE] RetryAttempt checkCommunicationBetweenLibAccessibility 02")
                Single.create { emitter: SingleEmitter<Boolean> ->
                    context.logi("[SPSUITE-TRACE] RetryAttempt checkCommunicationBetweenLibAccessibility 03")
                    try {
                        context.logi("[SPSUITE-TRACE] RetryAttempt checkCommunicationBetweenLibAccessibility 04")
                        Thread.sleep(RECEIVER_SUSCRIPTION_DELAY)
                        RequestIntent.requestHearbeat(context) { s ->
                            context.logi("[SPSUITE-TRACE] RetryAttempt checkCommunicationBetweenLibAccessibility 05")
                            closure?.invoke("Resultado exitoso")
                            context.logi("[SPSUITE-TRACE] Inside requestHearbeat callback")
                            emitter.onSuccess(true)
                        }
                    } catch (e: Exception) {
                        emitter.onError(e)

                    }
                }
            }.timeout(timeOut, java.util.concurrent.TimeUnit.MILLISECONDS)
                // .repeatUntil { Single.just((executionStartedAt + timeOut) < System.currentTimeMillis())}
        }
        /**
         * recupera información del dispositivo
         */
        @NotUIThread
        fun requestDeviceInfoBySuite(context: Context, timeOut : Long = 1000) : Single<DeviceDataDTO> {
            return Single.create { emitter: SingleEmitter<DeviceDataDTO> ->
                RequestIntent.requestShowDeviceDataBySuite(context, closure = {
                    it?.let {
                        context.logi("[Device Util] -->>> ${it}")
                        val deviceDataDTO = Gson().fromJson(it.trim(), DeviceDataDTO::class.java)
                        if(deviceDataDTO.imei.isEmpty() || deviceDataDTO.phoneNumber.isEmpty())
                            emitter.onError(Throwable("imei or phone number is empty"))
                        else
                            emitter.onSuccess(deviceDataDTO)
                    }
                })
            }.timeout(timeOut, java.util.concurrent.TimeUnit.MILLISECONDS)
        }




    }
}