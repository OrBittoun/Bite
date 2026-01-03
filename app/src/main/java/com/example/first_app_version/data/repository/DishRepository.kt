package com.example.first_app_version.data.repository

import android.app.Application
import com.example.first_app_version.data.local_db.KitchenDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DishRepository(application: Application) {

    private val dishDao = KitchenDataBase.getDataBase(application).dishesDao()

    fun getDishesForDishType(dishTypeId: Int) = dishDao.getDishesForDishType(dishTypeId)
    fun getDishById(dishId: Int) = dishDao.getDishById(dishId)

    suspend fun getDishImageRes(dishId: Int): Int? = withContext(Dispatchers.IO) {
        dishDao.getImageResForDish(dishId)
    }
}