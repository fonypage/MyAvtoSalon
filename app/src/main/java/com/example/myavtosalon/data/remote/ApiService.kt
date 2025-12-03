package com.example.myavtosalon.data.remote

import com.example.myavtosalon.data.local.Brand
import com.example.myavtosalon.data.local.CarModel
import com.example.myavtosalon.data.local.Client
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Интерфейс API для примера.
 * Здесь могли бы быть реальные запросы к серверу автосалона.
 */
interface ApiService {

    // Получить список марок
    @GET("brands")
    suspend fun getBrands(): List<Brand>

    // Получить список моделей
    @GET("models")
    suspend fun getModels(): List<CarModel>

    // Получить список клиентов
    @GET("clients")
    suspend fun getClients(): List<Client>
}

/**
 * созданиe Retrofit-сервиса.
 */
object ApiFactory {
    private const val BASE_URL = "https://example.com/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
