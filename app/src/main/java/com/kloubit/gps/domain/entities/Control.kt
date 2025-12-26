package com.kloubit.gps.domain.entities

import androidx.room.*
import com.kloubit.gps.data.room.converter.TimeConverter
import java.util.*
@Entity(tableName = Control.TABLE_NAME,
    indices = [Index(value = ["controlCode", "routeCode"], unique = true)]
)
@TypeConverters(TimeConverter::class)
class Control(@ColumnInfo(name = "controlCode") val controlCode: Int,                         //codControl
              @ColumnInfo(name = "controlName") val controlName: String,                      //nombreControl
              @ColumnInfo(name = "latitude") val latitude: Double,                            //latitud
              @ColumnInfo(name = "longitude") val longitude: Double,                          //longitud
              @ColumnInfo(name = "coverage") var coverage: Float = 0f,                        //cobertura
              @ColumnInfo(name = "side") val side: String,                                    //lado
              @ColumnInfo(name = "tourCode") val tourCode: Int,                               //codRecorrido
              @ColumnInfo(name = "routeCode") val routeCode: Int,                             //codRuta
              @ColumnInfo(name = "routeName") val routeName: String,                          //nombreRuta
              @ColumnInfo(name = "controlType") val controlType: Int,                         //codControlTipo
              @ColumnInfo(name = "orderNumber") val orderNumber: Int,                         //nroOrden
              @ColumnInfo(name = "isChangeSense") val isChangeSense: Boolean = false,             //cambioSentido
              @ColumnInfo(name = "isGoing") val isGoing: Boolean = false,                      //ida
              @ColumnInfo(name = "creationDate") var creationDate: Date? = Date(), //fechaCreacion
              @ColumnInfo(name = "controlAbbreviatedName") val controlAbbreviatedName: String,
){
    companion object {
        const val TABLE_NAME = "Control"
        const val GOING_TYPE = 1        //TIPO_IDA
        const val RETURN_TYPE = 0       //TIPO_VUELTA
//        const val CHANGE_TYPE_SENSE = 1 //TIPO_CAMBIO_SENTIDO_OK
        const val TERMINAL = 1   //TERMINAL
        const val CONTROL = 2    //CONTROL
        const val TRAIL = 3      //RASTRO
    }
    @Ignore
    var isIntersecting : Boolean = false

    @Ignore
    var distanceTo : Double = 0.0

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}