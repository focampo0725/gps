package com.kloubit.gps.infrastructure.business

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.di.qualifiers.NotUIThread
import com.kloubit.gps.infrastructure.stateful.AppState

interface ITrackProcessor {
    fun setOnLocation(location: Location, isFakeTrack: Boolean = false)
    fun resetValues()
}

/**
 * clase procesadora de coordenadas para el tracking a abx
 */
class TrackProcessorImpl(
    private val appRepository: AppRepository,
    private val appState: AppState,
    private val context: Context
) :
    ITrackProcessor {

    var locallyTrackCounter = 0 // 2, 4, 6 8 | 2, ...., 1800
    var locallyStoppedVehicleCounter = 0 // 2, 4, 6 8 | 2, ...., 1800
    @Volatile
    var currentTrackInterval = 0    // 8 or 1800 seconds
    var sumCountPeerTrack = 2   // divisible de 2000 o por defecto 2
    override fun setOnLocation(location: Location, isFakeTrack: Boolean) {
        TODO("Not yet implemented")
    }

    /**
     * sumCountPeerTrack el esl valor del contador q se usarà para la suma del contador de triangulaciòn del gps para luego
     * ser comparado por el smallInterl o largeInterval
     */
    @NotUIThread
//    private fun loadConfigurationIfIsNecessary(trackParams: TrackParamsDTO) {
//        if (currentTrackInterval == 0) {
//            currentTrackInterval = trackParams.smallInterval.toInt()
//            // calcula el valor agregado a cada suma de contadores (vehìculo detenido o triangulaciòn del gps)
//            appState.appParamsDTO?.locationParamsDTO?.interval?.let { interval ->
//                val isPair = (interval.toInt() % 2000) == 0
//                if (isPair) {
//                    sumCountPeerTrack = ((interval / 2000).toInt() * 2) // 2000 => 2, 4000 => 4,....
//                }
//            }
//        }
//    }

    /**
     * retorna true en caso de q se deba registrar el posteo #registerTrack
     */
//    @NotUIThread
//    fun checkTrackIntervalToRegister(trackParamsDTO: TrackParamsDTO, location: Location): Boolean {
//
//        appState.applicationIndicatorsState.postDelay(
//            Pair(
//                AppIndicatorStateConstants.SPEED_STATE,
//                (location.speed * KILOMETERS_HOURS).toInt()
//            ), 200
//        )
//
//        loadConfigurationIfIsNecessary(trackParamsDTO)
//
//
//        // lògica para el cambio de frecuencia
//        if ((location.speed * KILOMETERS_HOURS).toInt() <= trackParamsDTO.minimumSpeed) {
//            if (locallyStoppedVehicleCounter == trackParamsDTO.maximumTimeStopped) {
//                currentTrackInterval = trackParamsDTO.largeInterval.toInt()
//                locallyTrackCounter = 0 // reset the 1st counter (tracking)
//
//            }
//
//
//            locallyStoppedVehicleCounter += sumCountPeerTrack   // increment el 2nd counter
//
//        } else {  // en caso q el vehìculo q se encuentre en movimiento (> 4 km) entonces limpiamos el estado de los counters detenidos..
//            if (currentTrackInterval == trackParamsDTO.largeInterval.toInt()) {
//                currentTrackInterval =
//                    trackParamsDTO.smallInterval.toInt()    // set small interval : 8 seconds
//                locallyTrackCounter = 0 // reset the 1st counter (tracking)
//
//            }
//            locallyStoppedVehicleCounter = 0    // reset the 2nd counter
//        }
//        // lògica para la identificaciòn de tiempo cumplido
//        if (locallyTrackCounter == currentTrackInterval) {
//            locallyTrackCounter = 0 // reset counter
//            locallyTrackCounter += sumCountPeerTrack
//            appState.applicationIndicatorsState.postValue(
//                Pair(
//                    AppIndicatorStateConstants.LOCATION_STATE,
//                    AppIndicatorStateConstants.LOCATION_STATE_POSTING
//                )
//            )
//
//
//            return true
//        }
//        locallyTrackCounter += sumCountPeerTrack
//        appState.applicationIndicatorsState.postValue(
//            Pair(
//                AppIndicatorStateConstants.LOCATION_STATE,
//                AppIndicatorStateConstants.LOCATION_STATE_TRIANGULATING
//            )
//        )
//        return false
//    }

    /**
     * registra los tracks sucesivamente...
     */
//    @SuppressLint("SuspiciousIndentation")
//    @NotUIThread
//    fun registerTrack(trackSetting: TrackParamsDTO, location: Location, vehicle: Vehicle) {
//        val foundControl = appState.foundControl
//        val session = appState.session
//        val personCode = session?.personCode ?: 0
//
//
//        val track = Track(
//            battery = DeviceUtils.getBatteryLevel(context = context),
//            controlCode = foundControl.value?.controlCode ?: 0,
//            dispatchCode = vehicle.dispatchCode ?: 0,
//            personCode = personCode.toString(),
//            isWithinControl = foundControl.value?.isIntersecting ?: false,
//            latitude = location.latitude,
//            longitude = location.longitude,
//            postFrequency = currentTrackInterval,
//            precision = location.accuracy.toDouble().toRound(2).toString(),
//            routeName = vehicle.routeName,
//            routeSide = vehicle.sideName, // todo : <----no debera ser necesario se coordino con BD Jesus [JUAN-FIX]
//            speed = (location.speed*KILOMETERS_HOURS).toDouble().toRound(2).toDouble(),
//            unitCode = vehicle.unitCode,
//            serviceCode = session?.serviceCode ?: 0,
//            routeCode = vehicle.routeCode
//        )
//        xtuRepository.trackDao.insert(track)
//        appState.trackMutableLiveData.postValue(Pair(first = listOf(xtuRepository.trackDao.getLast()!!), second = false))  // notify to server
//    }

    /**
     * mètodo receptor de cada triangulaciòn del clientLocation.
     */
//    @NotUIThread
//    override fun setOnLocation(location: Location, isFakeTrack: Boolean) {
//        appState.appParamsDTO?.trackParamsDTO?.let {
//            if (it.isTrackEnabled) {
//                if (isFakeTrack)
//                    registerTrack(it, location, appState.vehicle!!) // solo por fines de desarrollo
//
//                if (checkTrackIntervalToRegister(it, location)) {
//                    registerTrack(it, location, appState.vehicle!!)
//                }
//            }
//        }
//    }

    /**
     * resetearà los valores cargados en memoria..
     */
    override fun resetValues() {
        currentTrackInterval = 0
    }

}