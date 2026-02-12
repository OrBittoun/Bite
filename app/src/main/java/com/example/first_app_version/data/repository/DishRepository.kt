package com.example.first_app_version.data.repository

import androidx.lifecycle.LiveData
import com.example.first_app_version.data.local_db.DishDao
import com.example.first_app_version.data.models.Dish
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishRepository @Inject constructor(
    private val dishDao: DishDao
) {
    fun getDishesForDishType(dishTypeId: Int): LiveData<List<Dish>> =
        dishDao.getDishesForDishType(dishTypeId)

    fun getDishById(dishId: Int): LiveData<Dish> =
        dishDao.getDishById(dishId)

    suspend fun getDishImageRes(dishId: Int): Int? {
        return dishDao.getImageResForDish(dishId)
    }
}