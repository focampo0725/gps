package com.kloubit.gps.data.room.dao

import androidx.room.*

@Dao
interface BaseDao <T>{

    companion object{
        val MAX_NUMBER_PENDING_ROWS = 400   // DEFAULT
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(entities: List<T>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllOrReplace(entities: List<T>): LongArray

    @Update
    fun update(vararg entity: T): Int

    @Delete
    fun delete(vararg entity: T): Int

}