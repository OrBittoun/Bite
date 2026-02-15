package com.example.first_app_version.ui.dish_display

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.repository.DishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DishDetailsViewModel @Inject constructor(
    private val repository: DishRepository
) : ViewModel() {

    fun dishById(dishId: Int): LiveData<Dish> = repository.getDishById(dishId)

    // הפונקציה של השותף לעדכון סטטוס מועדפים
    fun toggleFavorite(dishId: Int, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavoriteStatus(dishId, isFavorite)
        }
    }
}