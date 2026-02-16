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
    // Retrieves dishes filtered by their specific type ID
    fun getDishesForDishType(dishTypeId: Int): LiveData<List<Dish>> =
        dishDao.getDishesForDishType(dishTypeId)

    // Fetches full details for a single dish by its unique ID
    fun getDishById(dishId: Int): LiveData<Dish> =
        dishDao.getDishById(dishId)

    // Safely retrieves the local image resource ID for a dish from the background thread
    suspend fun getDishImageRes(dishId: Int): Int? = withContext(Dispatchers.IO) {
        dishDao.getImageResForDish(dishId)
    }

    // Updates whether a dish is marked as a favorite in the local database
    suspend fun updateFavoriteStatus(dishId: Int, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        dishDao.updateFavoriteStatus(dishId, isFavorite)
    }

    // Returns an observable list of all dishes saved to the user's favorites
    fun getFavoriteDishes(): LiveData<List<Dish>> = dishDao.getFavoriteDishes()

    // Inserts a new dish record into the database (e.g., when saving an API dish)
    suspend fun insertDish(dish: Dish) = withContext(Dispatchers.IO) {
        dishDao.insertDish(dish)
    }

    // Permanently removes a dish record from the local database
    suspend fun deleteDish(dish: Dish) = withContext(Dispatchers.IO) {
        dishDao.deleteDish(dish)
    }

    // Provides a live data list of all favorite dishes (alternative accessor)
    fun getAllFavorites(): LiveData<List<Dish>> {
        return dishDao.getFavoriteDishes()
    }
}