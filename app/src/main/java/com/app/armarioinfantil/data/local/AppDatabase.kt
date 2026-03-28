package com.app.armarioinfantil.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.armarioinfantil.data.local.dao.ChildDao
import com.app.armarioinfantil.data.local.dao.ClothingDao
import com.app.armarioinfantil.data.local.entity.ChildEntity
import com.app.armarioinfantil.data.local.entity.ClothingEntity

@Database(
    entities = [ChildEntity::class, ClothingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun childDao(): ChildDao
    abstract fun clothingDao(): ClothingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "armario_infantil_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
