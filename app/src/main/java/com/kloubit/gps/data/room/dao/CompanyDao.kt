package com.kloubit.gps.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kloubit.gps.domain.entities.Company


@Dao
interface CompanyDao : BaseDao<Company> {

    @Query("SELECT * FROM Company LIMIT 1")
    fun getTodo() : Company?
    @Query("SELECT * FROM Company")
    fun getAll(): List<Company>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEntityList(data: List<Company>)
    @Query("DELETE FROM Company")
    fun deleteAll()
}