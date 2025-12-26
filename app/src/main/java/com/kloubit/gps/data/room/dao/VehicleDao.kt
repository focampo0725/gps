package com.kloubit.gps.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.kloubit.gps.domain.entities.Vehicle

@Dao
interface VehicleDao : BaseDao<Vehicle>{
    @Query("DELETE FROM Vehicle")
    fun deleteAll() // todo : solo en la instalaciòn (1 sola vez) se hace el llamado.. recordar q tiene foreign keys en cascada : Session, ..

//                        update of vehicle : ticketTransactionLast, serializedControlCode, serializedTicketCode, driverBoxCode, routeName*, sideName*,routeTypeCode*
    /**
     * update de atributos de unidad cuando sesione correctamente el conductor..
     */
    //updateAperturaInicio
    @Query("""UPDATE Vehicle
                        SET
                            serializedControlCode = :serializedControlCode,
                            driverSessionCode = :driverSessionCode,
                            routeName = :routeName,
                            sideName = :sideName,
                            routeTypeCode = :routeTypeCode,
                            routeCode = :routeCode""")
    fun updateSessionedVehicle(serializedControlCode: Long,
                               driverSessionCode: Long,
                               routeName: String,
                               sideName: String,
                               routeTypeCode: Int,
                               routeCode: Int)

    @Query("SELECT * FROM Vehicle LIMIT 1")
    fun getLiveData(): LiveData<Vehicle?>


    /**
     * update de sentido/ida
     * {@link com.abx.mpos.activities.task.LocationTask}
     * @return int
     */
    @Query("""
            UPDATE Vehicle SET routeDirection = :routeDirection
            """)
    fun updateRouteDirection(routeDirection : Int)

    @Query("""
            UPDATE Vehicle SET routeTypeCode = :routeTypeCode
            """)
    fun updateRouteType(routeTypeCode : Int)

    // update dispatch code / cod salida programada
    @Query("""
            UPDATE Vehicle SET dispatchCode = :dispatchCode
            """)
    fun updateDispatchCode(dispatchCode : Long?)

    @Query("""
            UPDATE Vehicle SET idCar = :idCar
            """)
    fun updateTest(idCar : String?)



    // updateLado
    @Query("""
            UPDATE Vehicle SET sideName = :sideName
            """)
    fun updateSide(sideName : String)

    //getUnidad
    @Query("SELECT * FROM Vehicle LIMIT 1")
    fun get(): Vehicle


    // updateVehiclePropsById
    @Query("""
            UPDATE Vehicle 
            SET concessionaireName = :concessionaireName AND concessionaireRUC = :concessionaireRUC AND
            entityName = :entityName AND entityRUC = :entityRUC AND policy = :policy AND contactPhone = :contactPhone
            """)
    fun updateVehiclePropsById(concessionaireName : String, concessionaireRUC : String, entityName : String, entityRUC : String,
                              policy : String , contactPhone : String)


    // update driverSessionCode
    @Query("""
            UPDATE Vehicle SET driverSessionCode = :driverSessionCode
            """)
    fun updateDriverSessionCodeById(driverSessionCode : Long?)

    //////////////////////////////////////////////////////////////////////////////////////////
    // helpers para determinar si el cierre de sesiòn fue efectuado correctamente o no..
    @Query("SELECT * FROM Vehicle WHERE driverSessionCode IS NOT null")
    fun getDriverSessionCodeByIdNotDone(): Vehicle?
    //////////////////////////////////////////////////////////////////////////////////////////

    @Query("""
            UPDATE Vehicle SET isConfigured = :isConfigured
            """)
    fun updateIsConfigured(isConfigured : Boolean)

    //updateAperturaInicio
    @Query("""UPDATE Vehicle SET serializedControlCode = :serializedControlCode""")
    fun updateControlCode(serializedControlCode: Int)
}