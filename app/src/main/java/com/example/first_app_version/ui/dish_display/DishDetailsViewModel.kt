package com.example.first_app_version.ui.dish_display

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.repository.DishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DishDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DishRepository(application)

    fun dishById(dishId: Int): LiveData<Dish> = repository.getDishById(dishId)

    fun toggleFavorite(dishId: Int, isFavorite: Boolean) {
        // שימוש ב-Dispatchers.IO לכתיבה למסד הנתונים
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFavoriteStatus(dishId, isFavorite)
        }
    }
}