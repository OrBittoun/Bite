package com.example.first_app_version.ui.all_kitchens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.first_app_version.data.models.DishType

class SelectionViewModel : ViewModel() {

    private val _selectedDishId = MutableLiveData<Int>()

    //חלק ב
    private val _isFavoritesMode = MutableLiveData<Boolean>(false)
    val isFavoritesMode: LiveData<Boolean> = _isFavoritesMode
    val selectedDishId: LiveData<Int>
        get() = _selectedDishId

    fun setDishId(dishId: Int) {
        _selectedDishId.value = dishId
    }

    private val _selectedDishType = MutableLiveData<DishType>()
    val selectedDishType: LiveData<DishType>
        get() = _selectedDishType

    fun setDishType(dishType: DishType) {
        _selectedDishType.value = dishType
    }

    //חלק ב
    fun setFavoritesMode(isFavorites: Boolean) {
        _isFavoritesMode.value = isFavorites
    }
}


