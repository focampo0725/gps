package com.kloubit.gps.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.abx.shared.supportabx.extensions.toStringFormat
import com.kloubit.gps.domain.entities.Session
import java.util.*

@Dao
interface SessionDao : BaseDao<Session>{

    @Query("SELECT OP.* FROM Session OP INNER JOIN Vehicle UN ON OP.driverSessionCode = UN.driverSessionCode ORDER BY UN.id DESC LIMIT 1")
    fun getLiveData(): LiveData<Session?>

    /**
     * actualiza todos los pendientes a truncos cuando el conductor inicie el proceso de apertura manual..(login)
     */
    @Query("""UPDATE Session 
                    SET stateCode = ${Session.TRUNK}
                    WHERE stateCode = ${Session.PENDING}""")
    fun updateTrunkAllPendings()

    /**
     * Obtiene la ùltima apertura pendiente
     */
    //getAperturaPendiente3
    @Query("SELECT * FROM Session WHERE stateCode = ${Session.PENDING} ORDER BY id DESC LIMIT 1")
    fun getPending(): Session

    @Query("SELECT * FROM Session WHERE stateCode =${Session.OPERATING} ORDER BY id DESC LIMIT 1")
    fun getStatusSession(): Session

    //updateAperturaInicio
    @Query("""UPDATE Session 
                    SET driverSessionCode = :driverSessionCode, 
                        stateCode = ${Session.OPERATING},
                        startConfirmationDate = :startConfirmationDate,
                        driverName = :driverName,
                        personCode = :personCode
                    WHERE id = :id""")
    fun updateStartSession(id: Int,
                            driverSessionCode : Long,
                            driverName: String,
                            personCode: Int,
                            startConfirmationDate : Long = Date().time)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSession(session: Session): Long

    //update of session : status trunk, startConfirmationDate
    @Query("""UPDATE Session 
                    SET stateCode = ${Session.TRUNK},
                    startConfirmationDate = :startConfirmationDate
                    WHERE id = :id""")
    fun updateTrunkSessionById(id: Int, startConfirmationDate : String = Date().toStringFormat())



    @Query("""UPDATE Session
                    SET stateCode = ${Session.TRUNK},
                    driverSessionCode = null
                    WHERE id = :id""")
    fun updateTrunkDriverSessionCode(id: Int)

    //getAperturaByCCU
    @Query("SELECT * FROM Session WHERE driverSessionCode = :driverSessionCode ORDER BY id DESC LIMIT 1")
    fun getByDriverSessionCode(driverSessionCode: Long): Session?

    //update of session : startConfirmationDate
    @Query("""UPDATE Session 
                    SET finalConfirmationDate = :finalConfirmationDate
                    WHERE driverSessionCode = :driverSessionCode""")
    fun updateFinishSessionByDriverSessionCodeOnline(driverSessionCode: Long, finalConfirmationDate : Long = Date().time)

    //update of session : status finalized, finalDate
    @Query("""UPDATE Session 
                    SET stateCode = ${Session.FINALIZED},
                    finalDate = :finalDate,
                    latitudeEnd = :latitudeEnd,
                    longitudeEnd = :longitudeEnd
                    WHERE driverSessionCode = :driverSessionCode""")
    fun updateFinishSessionByDriverSessionCodeLocally(driverSessionCode: Long, finalDate :  Long = Date().time, latitudeEnd : String, longitudeEnd : String)

    //////////////////////////////////////////////////////////////////////////////////////////
    // helpers para determinar si el cierre de sesiòn fue efectuado correctamente o no..
    @Query("SELECT * FROM Session WHERE driverSessionCode = :driverSessionCode AND (stateCode != ${Session.FINALIZED} OR finalDate IS null OR latitudeEnd IS null OR longitudeEnd IS null)")
    fun getFinishSessionByDriverSessionCodeLocallyNotDone(driverSessionCode: Long): Session?

    @Query("SELECT * FROM Session WHERE driverSessionCode = :driverSessionCode AND finalConfirmationDate IS null")
    fun getFinishSessionByDriverSessionCodeOnlineNotDone(driverSessionCode: Long): Session?
    //////////////////////////////////////////////////////////////////////////////////////////

    @Query("SELECT * FROM Session ORDER BY id DESC")
    fun getAllListSession() : List<Session>

    //getAperturaActual
    @Query("SELECT OP.* FROM Session OP INNER JOIN Vehicle UN ON OP.driverSessionCode = UN.driverSessionCode ORDER BY UN.id DESC LIMIT 1")
    fun getCurrent(): Session?

    //getUltimaApertura
    @Query("SELECT * FROM Session WHERE driverSessionCode IS NOT null ORDER BY id DESC LIMIT 1")
    fun getLastActiveSession(): Session?

    // Obtiene ultima apertura sin importar si fue exitosa o si tuvo ventas o no..
    //getUltimaApertura
    @Query("SELECT * FROM Session ORDER BY id DESC LIMIT 1")
    fun getLastSession(): Session?

    // Actualiza la apertura trunca
    //updateAperturaTrunco
    @Query("""UPDATE Session 
                    SET stateCode = :stateCode
                    WHERE id = :id""")
    fun updateTrunk(id: Int,
                    stateCode: Int)

    // Actualiza el cierre de una apertura pendiente
    //updateAperturaCierreEstadoPendiente
    @Query("""UPDATE Session 
                    SET stateCode = :stateCode,
                        finalDate = :finalDate,
                        latitudeEnd = :latitudeEnd,
                        longitudeEnd = :longitudeEnd
                    WHERE id = :id""")
    fun updateClosurePendingStatus(id: Int,
                                   stateCode: Int,
                                   finalDate: String,
                                   latitudeEnd: String,
                                   longitudeEnd: String)

    // Actualiza el cierre de una apertura confirmado
    // updateAperturaCierreEstadoConfirmado
    @Query("""UPDATE Session 
                    SET stateCode = :stateCode
                    WHERE id = :id""")
    fun updateClosureConfirmedStatus(id: Int,
                                   stateCode: Int)

    // Eliminar aperturas pasadas los 15 días, ignorando el más reciente
    //deleteAperturasPasadas
    @Query("DELETE FROM Session WHERE startDate < date('now', '-30 day', 'localtime') AND id NOT IN (SELECT id FROM Session ORDER BY id DESC LIMIT 1)")
    fun removePast()

//    @Query( "SELECT date('now', '-15 day', 'localtime')")
//    fun fecha() : String

    /**
     * actualiza los atributos necesarios de una entidad operando y
     * la le asignará estado en proceso de cierre pendiente..
     */
    //updateAperturaOperando/updateAperturaFinalizado
    @Update
    fun update(session: Session) //todo observación con updateAperturaOperando y updateAperturaFinalizado

    @Query("DELETE FROM Session WHERE driverSessionCode = :driverSessionCode")
    fun removeByDriverSessionCode(driverSessionCode: Long)
}