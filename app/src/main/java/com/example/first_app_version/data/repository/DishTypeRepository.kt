package com.example.first_app_version.data.repository

import androidx.lifecycle.LiveData
import com.example.first_app_version.data.local_db.DishTypeDao
import com.example.first_app_version.data.models.DishType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishTypeRepository @Inject constructor(
    private val dishTypeDao: DishTypeDao
) {
    // Retrieves all categories of dishes associated with a specific kitchen ID
    fun getDishTypesForKitchen(kitchenId: Int): LiveData<List<DishType>> =
        dishTypeDao.getDishTypesForKitchen(kitchenId)
}