package com.example.first_app_version.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.models.DishType

class SelectionViewModel : ViewModel() {

    private val _selectedDishType = MutableLiveData<DishType>()
    val selectedDishType: LiveData<DishType> get() = _selectedDishType

    private val _selectedDish = MutableLiveData<Dish>()
    val selectedDish: LiveData<Dish> get() = _selectedDish

    fun setDishType(type: DishType) { _selectedDishType.value = type }
    fun setDish(dish: Dish) { _selectedDish.value = dish }
}