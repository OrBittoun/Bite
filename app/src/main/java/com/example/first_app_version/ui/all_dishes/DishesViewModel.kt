package com.example.first_app_version.ui.all_dishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.repository.DishRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DishesViewModel @Inject constructor(
    private val repository: DishRepository
) : ViewModel() {

    fun getDishesForType(dishTypeId: Int): LiveData<List<Dish>> =
        repository.getDishesForDishType(dishTypeId)
}