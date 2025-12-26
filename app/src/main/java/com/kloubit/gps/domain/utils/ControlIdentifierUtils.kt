package com.kloubit.gps.domain.utils

import android.location.Location
import com.abx.shared.supportabx.dto.LatLngDTO
import com.abx.shared.supportabx.extensions.toDistance
import com.kloubit.gps.domain.entities.Control
import com.kloubit.gps.domain.entities.Vehicle

class ControlIdentifierUtils {

    /**
     * find by route name and side
     * response of 3 types :
     * - intersected control
     * - nearest control but not intersected
     * - on not found control (optional)
     */
    fun findNearestControl(vehicle: Vehicle,
                           controlList : List<Control>,
                           location : Location
    ) : ResponseScannedControl{
//        filter control list to get only the controls of the road
        val vehicleControlsList = controlList.filter { it.side == vehicle.sideName && it.routeCode == vehicle.routeCode }.toMutableList()
        controlList.filter { it.side != vehicle.sideName && it.routeCode == vehicle.routeCode && it.controlType == Control.TERMINAL}.firstOrNull()?.let { opossiteTerminal ->
            vehicleControlsList.add(opossiteTerminal)
        }

        if(vehicleControlsList.isEmpty() || controlList.isEmpty()){
            return ResponseScannedControl(notFound = true)
        }

        val scannedControlsList = vehicleControlsList.map {
            val distance = it.toLatLng().toDistance(location.toLatLng())
            val isIntersected = (it.coverage >= distance)
            ScannedControl(distance = distance, control = it, isIntersected = isIntersected, location = location)
        }

        val intersectedControl = scannedControlsList.firstOrNull { it.isIntersected }
        val nearestControl = scannedControlsList.minBy { it.distance }

        intersectedControl?.apply {
            this.control.isIntersecting = true  // inyectamos el prop isIntersecting a true para que en las emisiones de tickets se puedan enviar en cada tx todo : necesario
            this.control.distanceTo = this.distance
        }

        nearestControl?.apply {
            this.control.isIntersecting = false // optional because its value is false by default
            this.control.distanceTo = this.distance
        }

        return ResponseScannedControl(notFound = false, intersectedControl = intersectedControl, nearestControl = nearestControl)
    }

    data class ResponseScannedControl(val notFound : Boolean, val intersectedControl : ScannedControl? = null, val nearestControl : ScannedControl? = null)

    data class ScannedControl(val distance : Double, val control: Control, val isIntersected : Boolean, val location : Location)

    fun Control.toLatLng() : LatLngDTO {
        return LatLngDTO(lat = this.latitude, lng = this.longitude)
    }

    fun Location.toLatLng() : LatLngDTO {
        return LatLngDTO(lat = this.latitude, lng = this.longitude)
    }

}