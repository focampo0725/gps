package com.kloubit.gps.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kloubit.gps.domain.entities.Dispatch

@Dao
interface DispatchDao:BaseDao<Dispatch> {
    @Query("SELECT * FROM Dispatch LIMIT 1")
    fun getDispatch() : Dispatch?

    @Query("SELECT * FROM Dispatch WHERE dispatchCode = :dispatchCode LIMIT 1")
    fun getDispatchFromDispatchCode(dispatchCode : Long) : Dispatch?

    @Query("SELECT * FROM Dispatch")
    fun getAll(): List<Dispatch>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEntityList(data: List<Dispatch>)
    @Query("DELETE FROM Dispatch")
    fun deleteAll()

    @Query("DELETE FROM Dispatch WHERE dispatchCode = :dispatchCode")
    fun deleteDispatchByDispatchCode(dispatchCode: Long)
}