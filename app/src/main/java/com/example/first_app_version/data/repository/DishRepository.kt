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

    // --- עדכון עבור מועדפים ---

    // פעולה לעדכון סטטוס המועדף (נעטוף ב-withContext כי זו פעולת כתיבה ל-DB)
    suspend fun updateFavoriteStatus(dishId: Int, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        dishDao.updateFavoriteStatus(dishId, isFavorite)
    }

    // שליפת ה-LiveData של המועדפים (כאן אין צורך ב-suspend כי LiveData מטפל בזה אסינכרונית)
    fun getFavoriteDishes() = dishDao.getFavoriteDishes()
}