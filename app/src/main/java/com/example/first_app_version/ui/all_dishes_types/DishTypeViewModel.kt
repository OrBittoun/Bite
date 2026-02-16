package com.example.first_app_version.ui.all_dishes_types

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.data.repository.DishTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DishesTypesViewModel @Inject constructor(
    private val repository: DishTypeRepository
) : ViewModel() {

    fun getDishTypesForKitchen(kitchenId: Int): LiveData<List<DishType>> =
        repository.getDishTypesForKitchen(kitchenId)
}