package com.kloubit.gps.domain.utils

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import com.kloubit.gps.R
import com.kloubit.gps.ui.main.splashscreen.SplashScreenActivity


class NotificationUtils {

    companion object{
        var NOTIFICATION_URGENT_ID = "ENDLESS SERVICE CHANNEL"
        /**
         * inicializa la notificaciòn en primer plano para que los servicios en segundo plano no crasheen como
         * LocationListener a partir de la versiòn 8.0+ de android.
         * Ademàs, el 'NOTIFICATION_URGENT_ID' se podrà utilizar desde otras notificaciones q se lancen en la app
         */
        fun init(context: Context, service : Service){
            service.startForeground(1, NotificationUtils().generateNotificationChannel(context))
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun generateNotificationChannel(context: Context): Notification {
        val title = "BiiX-Recharge"
        val description = "Service On"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(NOTIFICATION_URGENT_ID, title, NotificationManager.IMPORTANCE_LOW).let {
                it.description =description
                it.enableLights(false)
                it.lightColor = Color.BLUE
                it.vibrationPattern = longArrayOf(0)
                it.enableVibration(false)
                it.setShowBadge(true)
                it.setSound(null, null)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = Intent(context, SplashScreenActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(context, 0, notificationIntent, flags)
        }



        val builder : Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(context, NOTIFICATION_URGENT_ID) else Notification.Builder(context)

        return builder
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
//            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }
}