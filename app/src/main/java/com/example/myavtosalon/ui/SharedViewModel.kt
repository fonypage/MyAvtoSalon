package com.example.myavtosalon.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.myavtosalon.data.Repository
import com.example.myavtosalon.data.local.Brand
import com.example.myavtosalon.data.local.CarModel
import com.example.myavtosalon.data.local.Client
import kotlinx.coroutines.launch

/**
 * Общий ViewModel для всех фрагментов.
 * Хранит выбранную марку, модель и даёт доступ к данным через Repository.
 */
class SharedViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = Repository(app)

    // --------- ВЫБРАННЫЕ ЭЛЕМЕНТЫ ДЛЯ НАВИГАЦИИ ---------

    private val _selectedBrandId = MutableLiveData<Long?>()
    val selectedBrandId: LiveData<Long?> = _selectedBrandId

    private val _selectedModelId = MutableLiveData<Long?>()
    val selectedModelId: LiveData<Long?> = _selectedModelId

    fun selectBrand(id: Long) {
        _selectedBrandId.value = id
        _selectedModelId.value = null
    }

    fun selectModel(id: Long) {
        _selectedModelId.value = id
    }

    // --------- СПИСКИ ДАННЫХ ИЗ ROOM ---------

    // все бренды
    val brands: LiveData<List<Brand>> = repository.getBrands()

    // модели для выбранного бренда
    val modelsForSelectedBrand: LiveData<List<CarModel>> =
        _selectedBrandId.switchMap { brandId ->
            if (brandId == null) {
                MutableLiveData(emptyList())
            } else {
                repository.getModelsForBrand(brandId)
            }
        }

    // клиенты для выбранной модели
    val clientsForSelectedModel: LiveData<List<Client>> =
        _selectedModelId.switchMap { modelId ->
            if (modelId == null) {
                MutableLiveData(emptyList())
            } else {
                repository.getClientsForModel(modelId)
            }
        }

    // --------- ОПЕРАЦИИ ДОБАВЛЕНИЯ ДАННЫХ ---------

    fun addBrand(name: String, country: String? = null) {
        viewModelScope.launch {
            repository.addBrand(name, country)
        }
    }

    fun addModel(
        name: String,
        bodyType: String? = null,
        price: Double? = null
    ) {
        val brandId = _selectedBrandId.value ?: return
        viewModelScope.launch {
            repository.addModel(
                brandId = brandId,
                name = name,
                bodyType = bodyType,
                price = price
            )
        }
    }

    fun addClient(
        fullName: String,
        phone: String,
        purchaseDate: String? = null,
        dealPrice: Double? = null
    ) {
        val modelId = _selectedModelId.value ?: return
        viewModelScope.launch {
            repository.addClient(
                modelId = modelId,
                fullName = fullName,
                phone = phone,
                purchaseDate = purchaseDate,
                dealPrice = dealPrice
            )
        }
    }

    // --------- ПРИМЕР ВЫЗОВА СЕТИ (RETROFIT) ---------

    fun syncBrandsFromServer() {
        viewModelScope.launch {
            try {
                val remote = repository.loadBrandsFromServer()
                // здесь можно было бы сохранить remote в БД,
                // но для учебного задания достаточно самого вызова
            } catch (_: Exception) {
                // игнорируем ошибку сети
            }
        }
    }
}
