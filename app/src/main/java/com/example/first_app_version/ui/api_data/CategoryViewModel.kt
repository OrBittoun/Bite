package com.example.first_app_version.ui.api_data

import CategoryRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.first_app_version.data.remote.model.MealDetailsDto
import com.example.first_app_version.data.remote.model.MealDto
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CategoryRepository(application)

    val categories = repository.categoriesFromDb

    private val _showError = MutableLiveData<Boolean>(false)
    val showError: LiveData<Boolean> get() = _showError

    private val _meals = MutableLiveData<List<MealDto>>()
    val meals: LiveData<List<MealDto>> get() = _meals

    private val _mealDetails = MutableLiveData<MealDetailsDto?>()
    val mealDetails: LiveData<MealDetailsDto?> get() = _mealDetails

    fun fetchCategories() {
        viewModelScope.launch {
            repository.fetchCategoriesFromApi()
        }
    }

    fun getMealsByCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMealsByCategory(categoryName)
                if (response.isSuccessful) {
                    _meals.postValue(response.body()?.meals ?: emptyList()) //put the meals from API into local list, else- empty list
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchMealDetails(mealId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMealDetails(mealId)
                if (response.isSuccessful) {
                    _mealDetails.postValue(response.body()?.meals?.firstOrNull())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}