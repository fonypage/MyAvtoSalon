package com.example.myavtosalon.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Главная база данных приложения для автосалона.
 * Хранит марки, модели и клиентов.
 */
@Database(
    entities = [
        Brand::class,
        CarModel::class,
        Client::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun brandDao(): BrandDao
    abstract fun carModelDao(): CarModelDao
    abstract fun clientDao(): ClientDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tmp = INSTANCE
            if (tmp != null) return tmp

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "autosalon.db"      // имя файла БД
                )
                    .fallbackToDestructiveMigration() // на случай смены версии, чтобы не падало
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
