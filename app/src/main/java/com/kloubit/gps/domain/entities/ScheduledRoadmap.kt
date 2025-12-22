package com.kloubit.gps.domain.entities

import androidx.room.*
import com.kloubit.gps.data.room.converter.TimeConverter
import java.util.*

@Entity(tableName = ScheduledRoadmap.TABLE_NAME)

@TypeConverters(TimeConverter::class)
class ScheduledRoadmap(
    @ColumnInfo(name = "dispatchCode") val dispatchCode: String,
    @ColumnInfo(name = "controlCode") val controlCode: String,
    @ColumnInfo(name = "scheduledDateTime") val scheduledDateTime: Long,
    @ColumnInfo(name = "arrivalDateTime") val arrivalDateTime: Long,
    @ColumnInfo(name = "controlName") val controlName: String,
    @ColumnInfo(name = "controlType") val controlType: Int,
    @ColumnInfo(name = "orderNumber") val orderNumber: Int,
    @ColumnInfo(name = "sideName") val sideName: String,
    @ColumnInfo(name = "createAt") val createAt: Date = Date()
) {
    companion object {
        const val TABLE_NAME = "ScheduledRoadmap"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}