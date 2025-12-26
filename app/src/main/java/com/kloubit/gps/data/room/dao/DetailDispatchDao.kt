package com.kloubit.gps.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kloubit.gps.domain.dto.CountControlWithoutArriveAndArrive
import com.kloubit.gps.domain.entities.DetailDispatch


@Dao
interface DetailDispatchDao : BaseDao<DetailDispatch> {
    @Query("SELECT * FROM DetailDispatch")
    fun getAll(): List<DetailDispatch>


    @Query("SELECT * FROM DetailDispatch WHERE dispatchCode = :dispatchCode AND controlCode =:controlCode" )
    fun getDetailDisptachByDispatchCodeAnControlCode(dispatchCode: Long,controlCode: Int): DetailDispatch

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertEntityList(data: List<DetailDispatch>)

    @Query("DELETE FROM DetailDispatch")
    fun deleteAll()

    @Query("DELETE FROM DetailDispatch WHERE dispatchCode = :dispatchCode")
    fun deleteByDispatchCode(dispatchCode: Long)

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // helpers para determinar si el cierre de sesiòn fue efectuado correctamente o no..
    @Query("SELECT * FROM DetailDispatch WHERE dispatchCode = :dispatchCode AND (arrivalDateTime IS NULL OR arrivalDateTime = '')")
    fun getDetailDispatchWithoutArriveList(dispatchCode: Long): List<DetailDispatch>

    // recupera un conteo de los registros de controles marcados y no marcados
    @Query("""
        SELECT COUNT(CASE WHEN arrivalDateTime IS NOT NULL AND arrivalDateTime <> '' THEN 1 END) AS arrivedMarkedCount,
        COUNT(CASE WHEN arrivalDateTime IS NULL OR arrivalDateTime = '' THEN 1 END) AS withouArrivedMarkedCount
        FROM DetailDispatch WHERE dispatchCode = :dispatchCode
        """)
    fun getCountDetailDispatchWithoutArriveAndArrive(dispatchCode: Long): CountControlWithoutArriveAndArrive

    /**
     * Retorna un booleano q indica q si ya fueron marcados màs de la mitad de los controles de la salida programada..
     */
    fun isMinimunArrivalControls(dispatchCode: Long, tolerance: Int = 5): Boolean {
        val countControlArrided: CountControlWithoutArriveAndArrive =
            this.getCountDetailDispatchWithoutArriveAndArrive(dispatchCode = dispatchCode)
        return (countControlArrided.arrivedMarkedCount > 0
                && (countControlArrided.arrivedMarkedCount > (countControlArrided.withouArrivedMarkedCount - tolerance)));
    }

    /**
    UPDATE SalidaProgramada
    SET FechaHoraLLegada = '2023-11-14 15:14:00'
    WHERE FechaHoraLLegada IS NULL OR FechaHoraLLegada = '' AND CodControl = 36
     */
    /**
     * solo actualizará el control del despacho programado solo si es q el "arrival date time" estuviera sin un valor vàlido..
     */
    @Query("""
            UPDATE DetailDispatch 
            SET arrivalDateTime = :arrivalDateTime 
            WHERE controlCode = :controlCode AND dispatchCode = :dispatchCode AND (arrivalDateTime IS NULL OR arrivalDateTime = '' OR arrivalDateTime = 0)
            """)
    fun updateArrivalInControlOfDispatch(arrivalDateTime : Long, controlCode : Int, dispatchCode: Long)

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
    /***
     * Comprueba si la cantidad de controles marcados es superior a la mitad de la salida programada..
     * @param context {@link Context}
     * @return
     * @throws SQLException
    */
    public boolean isControlesMarcados(Context context) throws SQLException
    {
    String query =  String.format(Locale.getDefault(), "WHERE %s <> ''",
    SalidaProgramadaDataContract.FECHAHORALLEGADA.getValue());
    ArrayList<SalidaProgramada> alControlesMarcados = this.getAlEntidad(query, context);            // solo marcados
    ArrayList<SalidaProgramada> alControlesSalidaProgramada = this.getAlEntidad("", context);    // total
    return ( (alControlesMarcados.size()) > (alControlesSalidaProgramada.size() / 2) );
    }
     */
}