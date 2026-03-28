package com.app.armarioinfantil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.app.armarioinfantil.data.local.entity.ChildEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: ChildEntity): Long

    @Query("SELECT * FROM ninos ORDER BY fecha_creacion DESC")
    fun getAllChildren(): Flow<List<ChildEntity>>

    @Query("SELECT * FROM ninos WHERE id = :id")
    suspend fun getChildById(id: Long): ChildEntity?

    @Update
    suspend fun updateChild(child: ChildEntity)

    @Delete
    suspend fun deleteChild(child: ChildEntity)
}
