package com.kloubit.gps.infrastructure.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.kloubit.gps.infrastructure.clients.ClientLocation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var clientLocation: ClientLocation

    override fun onReceive(context: Context, intent: Intent) {
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        clientLocation.onLocationFromBroadcast(latitude, longitude)
    }
}
