package com.kloubit.gps.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.kloubit.gps.domain.entities.Track

@Dao
interface TrackDao : BaseDao<Track>{

    @Query("SELECT * FROM Track")
    fun getAll(): List<Track>

    @Query("SELECT * FROM Track WHERE flag = 0 ORDER BY id ASC LIMIT :limit")
    fun getTopListWithoutFlag(limit : Int = BaseDao.MAX_NUMBER_PENDING_ROWS): List<Track>

    @Query("SELECT * FROM Track ORDER BY createAt DESC LIMIT 1")
    fun getLast() : Track?

    @Query("SELECT * FROM Track WHERE id = :id")
    fun getEntityById(id : Int) : Track?

    @Query("DELETE FROM Track")
    fun deleteAll()

    @Query("""
            UPDATE Track SET flag = 1 WHERE id = :id
            """)
    fun updateIsSended(id : Int)

    @Query("SELECT * FROM Track WHERE flag = 0 ORDER BY id DESC LIMIT 1")
    fun getLastWithoutFlag(): Track?

    @Query("SELECT * FROM Track WHERE flag = 1 ORDER BY id DESC LIMIT 1")
    fun getLastWithConfirmationFlag(): Track?

}