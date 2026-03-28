package com.app.armarioinfantil.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.app.armarioinfantil.data.local.entity.ClothingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClothing(clothing: ClothingEntity): Long

    @Update
    suspend fun updateClothing(clothing: ClothingEntity)

    @Delete
    suspend fun deleteClothing(clothing: ClothingEntity)

    @Query("SELECT * FROM prendas WHERE nino_id = :childId")
    fun getClothingByChild(childId: Long): Flow<List<ClothingEntity>>

    @Query("SELECT * FROM prendas WHERE nino_id = :childId AND temporada = :season")
    fun getClothingByChildAndSeason(childId: Long, season: String): Flow<List<ClothingEntity>>

    @Query("SELECT * FROM prendas WHERE nino_id = :childId AND categoria = :category")
    fun getClothingByChildAndCategory(childId: Long, category: String): Flow<List<ClothingEntity>>

    @Query("SELECT * FROM prendas WHERE id = :id")
    suspend fun getClothingById(id: Long): ClothingEntity?

    @Query("UPDATE prendas SET nino_id = :newChildId WHERE id = :clothingId")
    suspend fun updateClothingChild(clothingId: Long, newChildId: Long)
}
