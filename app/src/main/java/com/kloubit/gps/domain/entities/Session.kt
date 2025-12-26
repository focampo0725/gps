package com.kloubit.gps.domain.entities

import android.annotation.SuppressLint
import androidx.room.*
import com.kloubit.gps.data.room.converter.TimeConverter
import java.util.*

@SuppressLint("SimpleDateFormat")
@Entity(tableName = Session.TABLE_NAME,
    indices = [Index(value = ["driverSessionCode"], unique = true)],
    foreignKeys = [
        ForeignKey(entity = Vehicle::class,
            parentColumns = arrayOf("unitCode"),
            childColumns = arrayOf("unitCode"),
            onDelete = ForeignKey.CASCADE)
    ]
)
@TypeConverters(TimeConverter::class)
class Session(@ColumnInfo(name = "driverCode") val driverCode: String,                               //codConductor
              @ColumnInfo(name = "serviceCode") val serviceCode: Int,
              @ColumnInfo(name = "driverName") val driverName: String? = "",                               //nomConductor
              @ColumnInfo(name = "personCode") val personCode: Long? = 0,                             //codigo de persona
              @ColumnInfo(name = "unitCode") val unitCode: Int,                                         // todo foreign key 1
              @ColumnInfo(name = "driverSessionCode") var driverSessionCode: Long? = null,                            //cod sesiòn conductor
              @ColumnInfo(name = "driverRoute") val driverRoute: String,                            //driverRoute
              @ColumnInfo(name = "stateCode") var stateCode: Int = PENDING,                           //codEstado
              @ColumnInfo(name = "startDate") var startDate:  Long = Date().time,                         //fechaHoraInicio
              @ColumnInfo(name = "startConfirmationDate") var startConfirmationDate: Long = Date().time, //* fechaHora de confirmaciòn inicial
              @ColumnInfo(name = "finalDate") var finalDate:  Long = Date().time,                                   //fechaHoraFin
              @ColumnInfo(name = "finalConfirmationDate") var finalConfirmationDate:Long = Date().time, //* fechaHora de confirmaciòn final
              @ColumnInfo(name = "creationDate") var creationDate: Date = Date(),                   //fechaCreacion
              @ColumnInfo(name = "latitudeStart") var latitudeStart: String,                    //latInicio
              @ColumnInfo(name = "longitudeStart") var longitudeStart: String,                  //lngInicio
              @ColumnInfo(name = "latitudeEnd") var latitudeEnd: String? = "",                        //latFin
              @ColumnInfo(name = "longitudeEnd") var longitudeEnd: String? = ""                         //lngFin
){
    companion object {
        const val TABLE_NAME = "Session"
        const val PENDING = 0    //PENDIENTE  // inicial
        const val TRUNK = -1     //TRUNCO     // final - error de contrasenia o error de peticiòn
        const val OPERATING = 1  //OPERANDO
        const val FINALIZED = 2  //FINALIZADO // final - cierre de apertura
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    //    convertidores a texto plano para el envìo al endpoint server..
//    @Ignore
//    var startDateAt = startDate.toStringFormat()
//    @Ignore
//    var finalDateAt = if(finalDate != null) finalDate!!.toStringFormat() else ""
//    @Ignore
//    var startConfirmationDateAt = if(startConfirmationDate != null) startConfirmationDate!!.toStringFormat() else ""
//    @Ignore
//    var finalConfirmationDateAt = if(finalConfirmationDate != null) finalConfirmationDate!!.toStringFormat() else ""
}