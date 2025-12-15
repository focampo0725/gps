package com.kloubit.gps.domain.entities

import androidx.room.*
import com.kloubit.gps.data.room.converter.TimeConverter
import java.util.*
@Entity(tableName = Company.TABLE_NAME)

@TypeConverters(TimeConverter::class)
class Company(@ColumnInfo(name = "companyCode") val companyCode: String,
              @ColumnInfo(name = "companyName") val companyName: String,
              @ColumnInfo(name = "creationDate") var creationDate: Date = Date())
{
    companion object {
        const val TABLE_NAME = "Company"
    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}

