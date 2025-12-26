package com.kloubit.gps.domain.entities

import androidx.room.*
import com.abx.shared.supportabx.extensions.toStringFormat
import com.kloubit.gps.data.room.converter.TimeConverter
import java.util.*

@Entity(tableName = Track.TABLE_NAME,
    indices = [Index(value = [ "unitCode"], unique = false, name = "index_track_unitCode")],
    foreignKeys = [
        ForeignKey(entity = Vehicle::class,
            parentColumns = arrayOf("unitCode"),  // vehicle
            childColumns = arrayOf("unitCode")    // track exception
        )
    ]
)
@TypeConverters(TimeConverter::class)
class Track(@ColumnInfo(name = "battery") var battery: Int = 0,                         //bateria
            @ColumnInfo(name = "latitude") var latitude: Double,                //latitud
            @ColumnInfo(name = "longitude") var longitude: Double,              //longitud
            @ColumnInfo(name = "createAt") var createAt: Date = Date(),                //fechaHora
            @ColumnInfo(name = "flag") var flag: Int = SINFLAG,                               //flagEnviado
            @ColumnInfo(name = "speed") var speed: Double,                        //velocidad
            @ColumnInfo(name = "controlCode") var controlCode: Int? = 0,                //codControl
            @ColumnInfo(name = "intersect") var isWithinControl: Boolean,                 //intersecta
            @ColumnInfo(name = "precision") var precision: String? = "",                //precision
            @ColumnInfo(name = "postFrequency") var postFrequency: Int? = 0,            //frecuenciaPosteo
            @ColumnInfo(name = "routeName") var routeName: String? = "",                  //nomRuta
            @ColumnInfo(name = "personCode") var personCode: String? = "",                //codConductor
            @ColumnInfo(name = "unitCode") var unitCode: Int? = 0,                       //codUnidad    //todo : <----no debera ser necesario
            @ColumnInfo(name = "routeSide") var routeSide: String? = "",                          //lado
            @ColumnInfo(name = "routeCode") var routeCode: Int,                                //cod ruta
            @ColumnInfo(name = "serviceCode") val serviceCode: Int,                  // cod servicio
            @ColumnInfo(name = "dispatchCode") var dispatchCode : Long ? = 0L,         //codSalidaServidor
            @ColumnInfo(name = "timeStamp") var timeStamp : Long = 0,                 //timeStamp
                    ){

    companion object {
        const val TABLE_NAME = "Track"
        const val ACTIVO = 1
        const val RECUPERADO = 23

        const val SINFLAG = 0
        const val CONFLAG = 1
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @Ignore
    var createAtFormatted = createAt.toStringFormat()
}