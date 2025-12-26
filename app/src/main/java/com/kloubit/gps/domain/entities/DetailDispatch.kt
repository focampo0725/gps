package com.kloubit.gps.domain.entities

import androidx.room.*
import com.kloubit.gps.data.room.converter.TimeConverter
import java.util.*

@Entity(tableName = DetailDispatch.TABLE_NAME,
    indices = [Index(
        value = [ "dispatchCode", "scheduledDateTime"],
        unique = true,
        name = "index_dispatchcode_scheduleddatetime_unique")
              ],
    foreignKeys = [
        ForeignKey(entity = Dispatch::class,
            parentColumns = arrayOf("dispatchCode"),  // dispatch
            childColumns = arrayOf("dispatchCode")    // detail dispatch
        )
//        , ForeignKey(entity = Control::class, // control      // TODO PENDING descomentar
//            parentColumns = arrayOf("controlCode"),  // control
//            childColumns = arrayOf("controlCode")    // detail dispatch
//        )
    ])
@TypeConverters(TimeConverter::class)
class DetailDispatch(
    @ColumnInfo(name = "dispatchCode") val dispatchCode: String,
    @ColumnInfo(name = "controlCode") val controlCode: String,
    @ColumnInfo(name = "scheduledDateTime") val scheduledDateTime: Date,
    @ColumnInfo(name = "arrivalDateTime") val arrivalDateTime: Date,
    @ColumnInfo(name = "passengersTotal") val passengersTotal: Int,
    @ColumnInfo(name = "createAt") val createAt: Date
) {
    companion object {
        const val TABLE_NAME = "DetailDispatch"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}