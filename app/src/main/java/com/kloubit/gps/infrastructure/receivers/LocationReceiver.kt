package com.kloubit.gps.infrastructure.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val latitude = intent?.getDoubleExtra("latitude", 0.0)
        val longitude = intent?.getDoubleExtra("longitude", 0.0)
        Toast.makeText(context, "latitude: $latitude, longitude: $longitude", Toast.LENGTH_SHORT).show()
        Log.d("[TestBrodcast]", "latitude: $latitude, longitude: $longitude")

        Toast.makeText(context, "latitude: $latitude + longitude: $longitude", Toast.LENGTH_SHORT).show()
    }
}