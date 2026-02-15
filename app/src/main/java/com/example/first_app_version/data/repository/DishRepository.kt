package com.example.first_app_version.data.repository

import androidx.lifecycle.LiveData
import com.example.first_app_version.data.local_db.DishDao
import com.example.first_app_version.data.models.Dish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun getDishImageRes(dishId: Int): Int? = withContext(Dispatchers.IO) {
        dishDao.getImageResForDish(dishId)
    }

    // --- התוספות של השותף עבור מועדפים ומנות מהרשת ---

    suspend fun updateFavoriteStatus(dishId: Int, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        dishDao.updateFavoriteStatus(dishId, isFavorite)
    }

    fun getFavoriteDishes(): LiveData<List<Dish>> = dishDao.getFavoriteDishes()

    suspend fun insertDish(dish: Dish) = withContext(Dispatchers.IO) {
        dishDao.insertDish(dish)
    }

    suspend fun deleteDish(dish: Dish) = withContext(Dispatchers.IO) {
        dishDao.deleteDish(dish)
    }

    fun getAllFavorites(): LiveData<List<Dish>> {
        return dishDao.getFavoriteDishes()
    }
}