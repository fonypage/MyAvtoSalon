package com.example.myavtosalon.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.myavtosalon.data.local.AppDatabase
import com.example.myavtosalon.data.local.Brand
import com.example.myavtosalon.data.local.CarModel
import com.example.myavtosalon.data.local.Client
import com.example.myavtosalon.data.remote.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository – слой между ViewModel и источниками данных (Room + Retrofit).
 */
class Repository(context: Context) {

    // Room
    private val db = AppDatabase.getInstance(context)
    private val brandDao = db.brandDao()
    private val modelDao = db.carModelDao()
    private val clientDao = db.clientDao()

    // --------- ЛОКАЛЬНЫЕ ДАННЫЕ (ROOM) ---------

    fun getBrands(): LiveData<List<Brand>> = brandDao.getAllBrands()

    fun getModelsForBrand(brandId: Long): LiveData<List<CarModel>> =
        modelDao.getModelsForBrand(brandId)

    fun getClientsForModel(modelId: Long): LiveData<List<Client>> =
        clientDao.getClientsForModel(modelId)

    suspend fun addBrand(name: String, country: String? = null) {
        withContext(Dispatchers.IO) {
            brandDao.insert(Brand(name = name, country = country))
        }
    }

    suspend fun addModel(
        brandId: Long,
        name: String,
        bodyType: String? = null,
        price: Double? = null
    ) {
        withContext(Dispatchers.IO) {
            modelDao.insert(
                CarModel(
                    name = name,
                    bodyType = bodyType,
                    price = price,
                    brandId = brandId
                )
            )
        }
    }

    suspend fun addClient(
        modelId: Long,
        fullName: String,
        phone: String,
        purchaseDate: String? = null,
        dealPrice: Double? = null
    ) {
        withContext(Dispatchers.IO) {
            clientDao.insert(
                Client(
                    fullName = fullName,
                    phone = phone,
                    purchaseDate = purchaseDate,
                    dealPrice = dealPrice,
                    modelId = modelId
                )
            )
        }
    }

    // --------- СЕТЕВЫЕ ДАННЫЕ (RETROFIT) ---------
    // Примеры, чтобы показать использование Retrofit в проекте

    suspend fun loadBrandsFromServer(): List<Brand> =
        withContext(Dispatchers.IO) {
            ApiFactory.api.getBrands()
        }

    suspend fun loadModelsFromServer(): List<CarModel> =
        withContext(Dispatchers.IO) {
            ApiFactory.api.getModels()
        }

    suspend fun loadClientsFromServer(): List<Client> =
        withContext(Dispatchers.IO) {
            ApiFactory.api.getClients()
        }
}
