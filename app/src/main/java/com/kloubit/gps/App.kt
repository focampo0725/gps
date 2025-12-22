package com.kloubit.gps

import android.app.Application
import android.content.*
import android.os.IBinder
import android.os.RemoteException
import android.util.Log

import com.abx.shared.supportabx.handlers.ReceiverManager
import com.abx.shared.supportabx.intents.SecurityIntentManager
import com.abx.shared.supportabx.intents.restartServiceAsync
import com.kloubit.gps.infrastructure.receivers.LocationReceiver
import com.kloubit.gps.infrastructure.receivers.PhoneCallReceiver
import com.kloubit.gps.infrastructure.receivers.SMSReceiver
import com.kloubit.gps.infrastructure.utils.TTSSingleton
import com.kp.ktsdkservice.printer.AidlPrinter
import com.kp.ktsdkservice.service.AidlDeviceService


import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    val TAG = "KioskModeApp"
    private val PACKAGENAME = "com.kp.ktsdkservice"
    private val CLASSNAME = "com.kp.ktsdkservice.service.DeviceService"
    var serviceManager: AidlDeviceService? = null
    var printer: AidlPrinter? = null
    companion object {
        lateinit var instance: com.kloubit.gps.App
        /**
         * deshabilita los widgets de bloqueo del status bar y navigation bar
         * Requiere supportsuite
         */
        fun disableSecurityStatusBarAndNavBar(context: Context){
            SecurityIntentManager.disableFloatingWidgetOverNavigationBar(context)
            SecurityIntentManager.disableFloatingTopBarTouchableView(context)
        }

        /**
         * habilita los widgets de bloqueo del status bar y navigation bar
         * Requiere supportsuite
         */
        fun enableSecurityStatusBarAndNavBar(context: Context){
            SecurityIntentManager.enableFloatingTopBarTouchableView(context)
            SecurityIntentManager.enableFloatingWidgetOverNavigationBar(context)
        }
    }

    @Inject
    @ApplicationContext
    lateinit var context : Context

    @Inject
    lateinit var phoneCallReceiver : PhoneCallReceiver

    @Inject
    lateinit var smsReceiver : SMSReceiver

    @Inject
    lateinit var locationReceiver: LocationReceiver



    override fun onCreate() {
        super.onCreate()
        com.kloubit.gps.App.Companion.instance = this
        bindService()
        // suscripciòn inicializada para comunicarse con el supportsuite..
        restartSuscriptions()
        TTSSingleton.getTTS(this)
    }

    fun bindService() {
        val intent = Intent()
        intent.setClassName(PACKAGENAME, CLASSNAME
        )
        val flag = bindService(intent, conn, BIND_AUTO_CREATE)
        if (flag) {
            Log.d(TAG, "Binder success!")
        } else {
            Log.d(TAG, "Binder fail!")
        }
    }

    private val conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, serviceBinder: IBinder) {
            if (serviceBinder != null) {
                Log.d(TAG, "aidlService service success!")
                serviceManager = AidlDeviceService.Stub.asInterface(serviceBinder)
                printer = try {
                    serviceManager?.getPrinter()?.let { AidlPrinter.Stub.asInterface(it) }
                } catch (e: RemoteException) {
                    Log.e(TAG, "Error al obtener impresora", e)
                    null
                }
                Log.d(TAG, "testing aidl printer")
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d(TAG, "AidlService  disconnected.")
        }
    }

    fun registerlocationReceiver(){
        locationReceiver = LocationReceiver()
        val filter = IntentFilter("abexa.action.LOCATION_UPDATE")
        registerReceiver(locationReceiver, filter)
    }
    fun restartSuscriptions(){
        ReceiverManager.unregisterSupportAbxReceiver(this)
        ReceiverManager.unregisterSupportSuiteReceiver(this)
        ReceiverManager.unregisterHiltBroadcastReceiverList(this)
        ReceiverManager.unregisterEventBusReceiver(this)

        ReceiverManager.startSupportAbxReceiver(this)
        ReceiverManager.startSupportSuiteReceiver(this)
        ReceiverManager.startEventBusReceiver(this)
        ReceiverManager.startHiltBroadcastReceiverList(context,phoneCallReceiver,smsReceiver)
    }
}