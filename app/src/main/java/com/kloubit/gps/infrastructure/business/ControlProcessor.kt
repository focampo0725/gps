package com.kloubit.gps.infrastructure.business

import android.content.Context
import android.location.Location
import android.util.Log
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.di.qualifiers.NotUIThread
import com.kloubit.gps.domain.entities.Control
import com.kloubit.gps.domain.entities.Track
import com.kloubit.gps.domain.entities.Vehicle
import com.kloubit.gps.domain.utils.ControlIdentifierUtils
import com.kloubit.gps.infrastructure.clients.ClientMQTT
import com.kloubit.gps.infrastructure.stateful.AppState

interface IControlProcessor{
    fun setOnLocation(location: Location) : Pair<ControlIdentifierUtils.ResponseScannedControl, Control?>
}

interface IControlEvents{
    fun onFoundControl(control: Control, vehicle: Vehicle, isExistsDispatch : Boolean)
    fun onChangeDirectionInRingTypeRoute(control: Control, vehicle: Vehicle)
    fun onLastControlInRingTypeRoute(control: Control, vehicle: Vehicle)
    //    fun onLastControlInConventionalTypeRoute(control: Control, vehicle: Vehicle)
    fun onArriveInConventionalTypeRoute(control: Control, vehicle: Vehicle)
    fun onArriveInRingTypeRoute(control: Control, vehicle: Vehicle)
}

/**
 * Clase procesadora de controles para la identificaciòn de controles y terminales
 */
class ControlProcessorImpl(private val appRepository: AppRepository, val controlIdentifierUtils: ControlIdentifierUtils, private val appState: AppState, private val context: Context) :
    IControlProcessor, IControlEvents {
    lateinit var trackList: List<Track>
    lateinit var clientMQTT: ClientMQTT
    var currentControlCode: Int = 0
    var currentTerminalCode: Int = 0

//    private lateinit var clientMQTT: ClientMQTT

    /**
     * recibe las coordenadas del dispositivo y las procesa para identificar el control o terminal con el q intercepta...
     */
    override fun setOnLocation(location: Location): Pair<ControlIdentifierUtils.ResponseScannedControl, Control?> {

//        var responseScannedControl = ControlIdentifierUtils.ResponseScannedControl(notFound = true)
//        var foundControlChanged : Control? = null   // contenedor que servirà para setear el app state cuando se pase al UIThread
//
//        // busca los controles màs cercanos y las precarga en memoria..
//        appState.controlList?.let {controlList ->
//            responseScannedControl = controlIdentifierUtils.findNearestControl(vehicle = appState.vehicle!!, location = location, controlList = controlList)
//        }
//
//        responseScannedControl.intersectedControl?.let {
//            if(appState.foundControl.value?.controlCode != it.control.controlCode) //  || (appState.foundControl.value?.isIntersecting == false) )
//            {
//                if(it.control.controlType == Control.TERMINAL)   // solo si es diferente al estado actual lo considerarà...
//                    processTerminalFound(context = context, foundTerminal = it.control, vehicle = appState.vehicle!!, isRingRouteType = (appState.vehicle!!.routeTypeCode == Vehicle.RING_ROUTE_TYPE))?.let {
//                        foundControlChanged = it
//                        foundControlChanged!!.isIntersecting = true
//                    }
//                if(it.control.controlType == Control.CONTROL)
//                    processControlFound(context = context, foundControl = it.control, vehicle = appState.vehicle!!, isRingRouteType = (appState.vehicle!!.routeTypeCode == Vehicle.RING_ROUTE_TYPE))?.let {
//                        foundControlChanged = it
//                        foundControlChanged!!.isIntersecting = true
//                    }
//            }
//        }

//        return Pair(first = responseScannedControl, second = foundControlChanged)  // response
        return Pair(
            ControlIdentifierUtils.ResponseScannedControl(notFound = true),
            null
        )
    }

    override fun onFoundControl(control: Control, vehicle: Vehicle, isExistsDispatch: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onChangeDirectionInRingTypeRoute(control: Control, vehicle: Vehicle) {
        TODO("Not yet implemented")
    }

    override fun onLastControlInRingTypeRoute(control: Control, vehicle: Vehicle) {
        TODO("Not yet implemented")
    }

    override fun onArriveInConventionalTypeRoute(control: Control, vehicle: Vehicle) {
        TODO("Not yet implemented")
    }

    override fun onArriveInRingTypeRoute(control: Control, vehicle: Vehicle) {
        TODO("Not yet implemented")
    }

    /**
         * Procesamiento de los controles encontrales, evaluando el tipo de ruta del vehìculo,
         * sentido de dirección y si es el ùltimo control de la ruta con un sentido de retorno.
         */
//    @NotUIThread
//    private fun processControlFound(context: Context, foundControl : Control, vehicle: Vehicle, isRingRouteType : Boolean): Control{
//
//
//        // se setearà el control de forma global siempre.. (sentido ida, cambio sentido , retorno) en cualquier tipo de ruta (convencional, anillo)
//        val isExistsDispatch = (vehicle.dispatchCode != null && vehicle.dispatchCode != 0L)
//        this.onFoundControl(control = foundControl, vehicle = vehicle, isExistsDispatch = isExistsDispatch)
//        // anillo
//        if(isRingRouteType)
//        {
//            val isVehicleInGoing = (vehicle.routeDirection == Control.GOING_TYPE)   // variable temporal .. (si el vehìculo se encuentra en el sentido de ida)
//
//            // si el control encontrado es de cambio de sentido "CS" y el vehìculo se encuentra en el sentido de ida... entonces se cambiarà el sentido (a RETORNO) del vehìculo..
//            if(foundControl.isChangeSense && isVehicleInGoing && isExistsDispatch)
//                this.onChangeDirectionInRingTypeRoute(control = foundControl, vehicle = vehicle)
//
//            // se obtiene el ultimo control de la ruta en base al nùmero de orden màs alto..
//            val lastControl : Control? = xtuRepository.controlDao.getLastControlByRouteAndSide(routeName = vehicle.routeName, side = vehicle.sideName)    // last control of the road.. (with the order number higher)
//
//            // si el control encontrado es el ùltimo control de la ruta y el vehìculo se encuentra en el sentido de retorno, se seteará el control encontrado de forma global..
//            if(lastControl?.controlCode == foundControl.controlCode
//                && lastControl.routeCode == foundControl.routeCode
//                && isExistsDispatch
//                && vehicle.routeDirection == Control.RETURN_TYPE)
//            {
//                this.onLastControlInRingTypeRoute(control = foundControl, vehicle = vehicle)
//            }
//        }
//        //[Franco] : Se comento porque no tendra aun accion encontrar el último control
////        if (!isRingRouteType) {
////            val lastControlConvencional : Control? = xtuRepository.controlDao.getLastControlConvencionalByRouteAndSide( side = vehicle.sideName)
////            if(lastControlConvencional!!.orderNumber == foundControl.orderNumber
////            ) {
////                this.onLastControlInConventionalTypeRoute(control = foundControl,vehicle = vehicle)
////            }
////        }
//
//        return foundControl
//    }
//


        /**
         * TODO ------------------------------------------------------------------------------------------------------------------------------------------------------------
         * TODO ------------------------------------------------------------------------------------------------------------------------------------------------------------
         * TODO : AGREGAR REGISTRO DE DESPACHOS EN LA TABLA "DISPATCH" (listo) -> se creò una tabla detalle para despacho : Dispatch && DetailDispatch
         * TODO : CREAR UNA WEBAPI PARA CARGAR DESPACHOS ---- (listo)
         * TODO : IMPLEMENTAR A LA LÒGICA DE LOCALIZACION EL USO DEL RECURSO DESPACHO
         * TODO : REVISAR NUEVAMENTE EL MÈTODO "processTerminalFound" Y "processControlFound" PARA QUE SEAN MÀS EFICIENTES COMPARANDOLOS CON LOCATIONLISTENER DEL GPS ABEJA
         * TODO ------------------------------------------------------------------------------------------------------------------------------------------------------------
         * TODO ------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

        /**
         * NOT UI-THREAD
         * procesarà el nuevo control (TIPO TERMINAL) encontrado dependiendo del tipo de ruta (anillo, convencional) que tiene configurado el vehicle..
         */

//    private fun processTerminalFound(context: Context, foundTerminal : Control, vehicle: Vehicle, isRingRouteType : Boolean) : Control?{
//        val isExistsDispatch = (vehicle.dispatchCode != null && vehicle.dispatchCode != 0L)
//        this.onFoundControl(control = foundTerminal, vehicle = vehicle, isExistsDispatch = isExistsDispatch)
//
//        if(!isRingRouteType &&  // RUTA CONVENCIONAL
//            isExistsDispatch &&
//            foundTerminal.routeCode == vehicle.routeCode &&
//            foundTerminal.side != vehicle.sideName)  // misma ruta pero diferente lado..
//            this.onArriveInConventionalTypeRoute(control = foundTerminal, vehicle = vehicle)    // terminal de lado contrario con despacho existente en ruta convencional
//
//        if(isRingRouteType &&   // RUTA TIPO ANILLO
//            // nota : en caso de q el vehìculo se encuentre en el sentido de RETORNO/RETURN y fuera una ruta de tipo ANILLO/RING ps haremos el UPDATE a sentido de IDA/GOING
//            (xtuRepository.detailDispatchDao.isMinimunArrivalControls(dispatchCode = vehicle.dispatchCode ?: 0L) || vehicle.routeDirection == Control.RETURN_TYPE ) &&
//            isExistsDispatch
//        )
//            this.onArriveInRingTypeRoute(control = foundTerminal, vehicle = vehicle)    // terminal encontrado con ruta q es ANILLO y ya marcò màs de la mitad de los controles o el sentido de direcciòn es de RETORNO..
//        return foundTerminal
//    }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////// /////////////////////////////////////////////////////////////////////////

        /**
         * actualiza la fecha y hora (timestamp) del control del detailDispatch si es q aùn no tiene algùn valor vàlido.
         */


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }



