package com.example.first_app_version.ui.dish_display

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.repository.DishRepository

class DishDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DishRepository(application)
    fun dishById(dishId: Int): LiveData<Dish> = repository.getDishById(dishId)
}