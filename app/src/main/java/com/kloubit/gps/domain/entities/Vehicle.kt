package com.kloubit.gps.domain.entities

import androidx.room.*
import com.kloubit.gps.data.room.converter.TimeConverter
import java.util.*

@Entity(tableName = Vehicle.TABLE_NAME,
    indices = [Index(value = ["unitCode"], unique = true),
        Index(value = ["unitCode", "deviceCode"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(entity = Session::class,
            parentColumns = arrayOf("driverSessionCode"),
            childColumns = arrayOf("driverSessionCode")),
        ForeignKey(entity = Dispatch::class,
            parentColumns = arrayOf("dispatchCode"),
            childColumns = arrayOf("dispatchCode"))
    ]
)
@TypeConverters(TimeConverter::class)
class Vehicle(@ColumnInfo(name = "unitCode") var  unitCode: Int,                                 //codUnidad
              @ColumnInfo(name = "deviceCode") var deviceCode: Int,                            //mg
              @ColumnInfo(name = "companyCode") var companyCode: Int,                           //idEmpresa
              @ColumnInfo(name = "serializedControlCode") var serializedControlCode: Int = 0, //controlCode de serializado
              @ColumnInfo(name = "idCar") var idCar: String,                                    //nombrePlaca
              @ColumnInfo(name = "isConfigured") var isConfigured: Boolean,                     //configurado
              @ColumnInfo(name = "concessionaireName") var concessionaireName: String,          //nombreConcesionario
              @ColumnInfo(name = "concessionaireRUC") var concessionaireRUC: String,            //RUCConcesionario
              @ColumnInfo(name = "entityName") var entityName: String,                          //nombreEntidad
              @ColumnInfo(name = "entityRUC") var entityRUC: String,                            //RUCEntidad
              @ColumnInfo(name = "registrationDate") var registrationDate: Date = Date(),       //fechaHoraRegistro
              @ColumnInfo(name = "driverSessionCode") var driverSessionCode: Long? = null,       //codCajaGestionConductor // todo foreign key 1 [Puede ser null o deberà tener un valor q coincida con el de la tabla session]
              @ColumnInfo(name = "routeName") var routeName: String,                            //nombreRuta
              @ColumnInfo(name = "sideName") var sideName: String,                              //nombreLado
              @ColumnInfo(name = "policy") var policy: String,                                  //poliza
              @ColumnInfo(name = "contactPhone") var contactPhone: String,                      //telefonoContacto
              @ColumnInfo(name = "padronName") var padronName: String,                          //nombrePadron
              @ColumnInfo(name = "routeDirection") var routeDirection: Int = Control.GOING_TYPE, //ida
              @ColumnInfo(name = "routeTypeCode") var routeTypeCode: Int,                        //codTipoRuta
              @ColumnInfo(name = "routeCode") var routeCode: Int,                                //nombreRuta
              @ColumnInfo(name = "dispatchCode") var dispatchCode: Long?                          //cod salida programada
){

    companion object {
        const val TABLE_NAME = "Vehicle"

        const val RING_ROUTE_TYPE = 2           //TIPO_ANILLO
        const val CONVENTIONAL_ROUTE_TYPE = 1        //TIPO_CONVENCIONAL

        const val SIDE_GOING_TYPE = "A"          //TIPO_ANILLO
        const val SIDE_RETURN_TYPE = "B"        //TIPO_CONVENCIONAL
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}