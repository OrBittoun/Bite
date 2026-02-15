package com.example.first_app_version.ui.api_data

import com.example.first_app_version.data.repository.CategoryRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.first_app_version.data.remote.model.MealDetailsDto
import com.example.first_app_version.data.remote.model.MealDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

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
                    _meals.postValue(response.body()?.meals ?: emptyList())
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

    fun fetchRandomMeal() {
        viewModelScope.launch {
            try {
                val response = repository.getRandomMeal()
                if (response.isSuccessful && !response.body()?.meals.isNullOrEmpty()) {
                    _mealDetails.postValue(response.body()?.meals?.get(0))
                }
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error fetching random meal", e)
            }
        }
    }
}