package com.example.first_app_version.ui.all_kitchens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.first_app_version.data.models.DishType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectionViewModel @Inject constructor() : ViewModel() {

    private val _selectedDishId = MutableLiveData<Int>()
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

    // --- התוספות של השותף עבור ניהול מצב מסך המועדפים ---
    private val _isFavoritesMode = MutableLiveData<Boolean>(false)
    val isFavoritesMode: LiveData<Boolean> = _isFavoritesMode

    fun setFavoritesMode(isFavorites: Boolean) {
        _isFavoritesMode.value = isFavorites
    }
}