package com.kloubit.gps.infrastructure.clients

import android.content.Context
import android.location.Location
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.domain.annotations.UIThread
import com.kloubit.gps.infrastructure.business.IControlProcessor
import com.kloubit.gps.infrastructure.business.ITrackProcessor
import com.kloubit.gps.infrastructure.stateful.AppState

class ClientLocation(
    private val context: Context,
    private val appRepository: AppRepository,
    private val appState: AppState,
    private val trackProcessor: ITrackProcessor,
    private val controlProcessor: IControlProcessor
) {

    /** intervalo mínimo entre ubicaciones (ms) */
    private val minIntervalMs = 8000L
//        appState.appParamsDTO?.locationParamsDTO?.interval ?: 8000L

    private var lastAcceptedTime = 0L
    private var isActive = true
    var isSimulateLocation = false

    /**
     * Entrada ÚNICA de ubicación desde Broadcast
     */
    fun onLocationFromBroadcast(
        latitude: Double,
        longitude: Double
    ) {

        if (!isActive) return

        val now = System.currentTimeMillis()

        // control de intervalo
        if (now - lastAcceptedTime < minIntervalMs) return

        val location = Location("broadcast").apply {
            this.latitude = latitude
            this.longitude = longitude
            this.time = now
        }

        if (!isValid(location)) return

        lastAcceptedTime = now

        if (!isSimulateLocation) {
            setOnLocation(location)
        }
    }


    /**
     * Procesamiento central de ubicación
     */
    private fun setOnLocation(location: Location, isFakeTrack: Boolean = false) {
        appState.location = location
//        appState.foundLocationLastDate = Date()
//
//        doAsynTask({
//            trackProcessor.setOnLocation(location, isFakeTrack)
//            controlProcessor.setOnLocation(location)
//        }, { responsesPair ->
//            responsesPair.second?.let {
//                logi(
//                    "[ControlProcessor] control => ${it.controlName}, " +
//                            "isIntersecting => ${it.isIntersecting}"
//                )
//                appState.foundControl.postValue(it)
//            }
//        })
    }

    private fun isValid(location: Location): Boolean {
        return location.latitude != 0.0 &&
                location.longitude != 0.0
    }

    fun stop() {
        isActive = false
    }

    fun start() {
        isActive = true
    }
}