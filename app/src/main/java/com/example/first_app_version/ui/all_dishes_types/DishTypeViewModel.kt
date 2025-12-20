package com.example.first_app_version.ui.all_dishes_types

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.data.repository.DishTypeRepository

class DishesTypesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DishTypeRepository(application)

    fun getDishTypesForKitchen(kitchenId: Int): LiveData<List<DishType>> =
        repository.getDishTypesForKitchen(kitchenId)
}