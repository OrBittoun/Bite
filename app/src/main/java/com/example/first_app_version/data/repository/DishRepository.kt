package com.example.first_app_version.data.repository

import android.app.Application
import com.example.first_app_version.data.local_db.KitchenDataBase

class DishRepository(application: Application) {

    private val dishDao = KitchenDataBase.getDataBase(application).dishesDao()

    fun getDishesForDishType(dishTypeId: Int) =
        dishDao.getDishesForDishType(dishTypeId)
}