package com.example.myavtosalon.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Таблица "марки автомобилей"
 */
@Entity(tableName = "brands")
data class Brand(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val country: String? = null
)

/**
 * Таблица "модели автомобилей"
 * Связана с Brand (одна марка -> много моделей)
 */
@Entity(
    tableName = "car_models",
    foreignKeys = [
        ForeignKey(
            entity = Brand::class,
            parentColumns = ["id"],
            childColumns = ["brandId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("brandId")]
)
data class CarModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val bodyType: String? = null,   // тип кузова: седан, хэтчбек и т.п.
    val price: Double? = null,      // ориентировочная цена
    val brandId: Long               // внешний ключ на Brand
)

/**
 * Таблица "клиенты"
 * Связана с CarModel (одна модель -> много клиентов)
 */
@Entity(
    tableName = "clients",
    foreignKeys = [
        ForeignKey(
            entity = CarModel::class,
            parentColumns = ["id"],
            childColumns = ["modelId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("modelId")]
)
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val fullName: String,   // ФИО клиента
    val phone: String,      // телефон (для Intent звонка)
    val purchaseDate: String? = null, // дата покупки (строкой, чтобы не усложнять)
    val dealPrice: Double? = null,    // цена сделки
    val modelId: Long               // внешний ключ на CarModel
)
