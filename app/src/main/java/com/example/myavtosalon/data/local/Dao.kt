package com.example.myavtosalon.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * DAO для таблицы Brand (марки автомобилей)
 */
@Dao
interface BrandDao {

    @Query("SELECT * FROM brands ORDER BY name")
    fun getAllBrands(): LiveData<List<Brand>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brand: Brand): Long

    @Update
    suspend fun update(brand: Brand)

    @Delete
    suspend fun delete(brand: Brand)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Brand>)

    @Query("DELETE FROM brands")
    suspend fun deleteAll()
}

/**
 * DAO для таблицы CarModel (модели автомобилей)
 */
@Dao
interface CarModelDao {

    // Все модели конкретной марки
    @Query("SELECT * FROM car_models WHERE brandId = :brandId ORDER BY name")
    fun getModelsForBrand(brandId: Long): LiveData<List<CarModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: CarModel): Long

    @Update
    suspend fun update(model: CarModel)

    @Delete
    suspend fun delete(model: CarModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CarModel>)

    @Query("DELETE FROM car_models")
    suspend fun deleteAll()

}

/**
 * DAO для таблицы Client (клиенты)
 */
@Dao
interface ClientDao {

    // Все клиенты, купившие конкретную модель
    @Query("SELECT * FROM clients WHERE modelId = :modelId ORDER BY fullName")
    fun getClientsForModel(modelId: Long): LiveData<List<Client>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: Client): Long

    @Update
    suspend fun update(client: Client)

    @Delete
    suspend fun delete(client: Client)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Client>)

    @Query("DELETE FROM clients")
    suspend fun deleteAll()

}
