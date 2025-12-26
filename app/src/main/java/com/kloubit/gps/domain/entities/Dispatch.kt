package com.kloubit.gps.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kloubit.gps.data.room.converter.TimeConverter
import java.util.*

@Entity(tableName = Dispatch.TABLE_NAME,
    indices = [Index(value = ["dispatchCode"], unique = true)]
)
@TypeConverters(TimeConverter::class)
class Dispatch(
    @ColumnInfo(name = "dispatchCode") val dispatchCode: String,
    @ColumnInfo(name = "createAt") val createAt: Date
) {
    companion object {
        const val TABLE_NAME = "Dispatch"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}