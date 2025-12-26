package com.kloubit.gps.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.kloubit.gps.domain.entities.Control

@Dao
interface ControlDao  : BaseDao<Control>{
    @Query("DELETE FROM Control")
    fun deleteAll() // todo : delete en cada apertura.. la tabla TicketControl tiene onCascade in Delete (warning)
    @Query("SELECT * FROM Control")
    fun getAllLiveData(): LiveData<List<Control>>
    /**
     *  Obtiene el ultimo control por el lado de la ruta
     */
    //getControlFinalByRutaLado

    @Query("SELECT * FROM Control WHERE side = :side AND routeName = :routeName ORDER BY orderNumber DESC LIMIT 1")
    fun getLastControlByRouteAndSide(routeName: String, side: String): Control?



    // segundo query

    @Query("SELECT * FROM Control WHERE side = :side  ORDER BY orderNumber DESC LIMIT 1")
    fun getLastControlConvencionalByRouteAndSide(side: String): Control?


    /**
     *  Obtiene el ultimo control por el lado de la ruta
     */
    //getControlFinalByRutaLado
    @Query("SELECT * FROM Control WHERE controlCode = :controlCode LIMIT 1")
    fun getByControlCode(controlCode: Int): Control?

    /**
     *  Obtiene todos los controles de tipo TERMINAL
     */
    //getAlControlTerminal
    @Query("SELECT * FROM Control WHERE controlType = ${Control.TERMINAL}")
    fun getAllTerminalControl(): List<Control>

    /**
     * Elimina todos los registros
     */
    //deleteControl
    @Query("DELETE FROM Control")
    fun truncate(): Int

    /**
     *  Obtiene todos los controles de tipo TERMINAL
     */
    //getAlControl
    @Query("SELECT * FROM Control")
    fun getAllControl(): List<Control>

    @Query("SELECT * FROM Control WHERE controlCode =:controlCode LIMIT 1")
    fun getByCode(controlCode : Int): Control

    /**
     *  Obtiene todos los controles por la tarifa
     */
    //getAlControlPorTarifa
    @Query("SELECT * FROM Control WHERE controlCode IN (:initialControlCode, :maxControlCode)")
    fun getAllControlByRate(initialControlCode: Int, maxControlCode: Int): List<Control>

    //Ultimo control de cada lado
    @Query("SELECT * FROM Control WHERE routeName =:routeName AND side =:sideName")
    fun getAllControlByRate(routeName: String, sideName : String): List<Control>


}