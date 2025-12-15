package com.kloubit.gps.data.common

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import com.abx.shared.supportabx.extensions.doAsynTask
import com.abx.shared.supportabx.extensions.logi
import com.kloubit.gps.domain.service.ServiceState
import com.kloubit.gps.domain.service.setServiceState
import com.kloubit.gps.domain.utils.NotificationUtils
import com.kloubit.gps.infrastructure.clients.ClientMQTT
import com.kloubit.gps.infrastructure.clients.ClientObserver
import com.kloubit.gps.infrastructure.stateful.AppState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class AppService :Service(){

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var appState: AppState

    @Inject
    lateinit var clientMQTT: ClientMQTT

    @Inject
    lateinit var clientObserver : ClientObserver


    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false

    override fun onCreate() {
        super.onCreate()
        NotificationUtils.init(this, this)
        startService()
        context.logi("[PersonalIdentifier] -> servicio creado")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null) {
            when (intent.action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> logi("This should never happen. No action in the received intent")
            }
        } else {
            logi(
                "with a null intent. It has been probably restarted by the system."
            )
        }
        return START_STICKY
    }

    private fun startService() {
        if (isServiceStarted) return
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)
        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                acquire()
            }
        }
        try {

            clientObserver.onStart(this)


            doAsynTask({
                Thread.sleep(5000)
            }, {
                clientMQTT.onStart(this)
            })
//            if(BuildConfig.DEBUG)   // todo : solo para fines de desarrollo està aùn el clientbroker..
//            clientBixolonDriver.onStart(this)

        }catch (e : java.lang.Exception){
            e.printStackTrace()
        }
    }



    private fun stopService() {

        try {
            clientObserver.onDestroy()

            clientMQTT.onDestroy()
        }   catch (e : java.lang.Exception){
            e.printStackTrace()
        }
        try {
            wakeLock?.let {
                if (it.isHeld) {it.release()}
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            logi("Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}