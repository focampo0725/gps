package com.kloubit.gps.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kloubit.gps.domain.entities.ScheduledRoadmap

@Dao
interface ScheduledRoadmapDao : BaseDao<ScheduledRoadmap> {

    @Query("DELETE FROM ScheduledRoadmap")
    fun deleteAll()


    @Query("UPDATE ScheduledRoadmap SET arrivalDateTime = :arrivalTime WHERE id = :id")
    fun markArrival(id: Int, arrivalTime: Long)


    /**
     * Devuelve una ventana de controles de 5 elementos, usando el número de orden
     * y el dispatchCode. Ajusta la ventana si estamos al inicio o al final de la ruta.
     */
//
//    @Query(
//        """
//    SELECT SR.scheduledDateTime,
//           SR.controlCode,
//           C.controlName,
//           C.controlType,
//           C.controlAbbreviatedName,
//           SR.arrivalDateTime
//    FROM ScheduledRoadmap SR
//    JOIN Control C ON SR.controlCode = C.controlCode
//    WHERE C.orderNumber >= :startOrder
//      AND C.orderNumber <= :endOrder
//    ORDER BY C.orderNumber DESC;
//    """
//    )
//    fun getScheduledWindow(
//        startOrder: Int,
//        endOrder: Int,
//    ): List<LinearControlDTO>
//
//
//    @Query("""SELECT * FROM ScheduledRoadmap WHERE arrivalDateTime IS NOT NULL AND arrivalDateTime != 0
//        ORDER BY orderNumber DESC
//        LIMIT 1
//
//    """)
//    fun currentControl() : ScheduledRoadmap?
//
//    @Query(
//        """
//    SELECT *
//    FROM (
//        SELECT SR.dispatchCode,
//               SR.controlCode,
//               SR.scheduledDateTime,
//               C.controlName,
//               C.controlType,
//               C.orderNumber
//        FROM ScheduledRoadmap SR
//        JOIN Control C ON SR.controlCode = C.controlCode
//        ORDER BY C.orderNumber ASC
//        LIMIT 5
//    ) AS sub
//    ORDER BY sub.orderNumber DESC
//    """
//    )
//    fun getDefaultWindow(): List<ScheduledRoadmapDTO>


}