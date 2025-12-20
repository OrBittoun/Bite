package com.example.first_app_version.data.repository

import android.app.Application
import com.example.first_app_version.data.local_db.DishTypeDao
import com.example.first_app_version.data.local_db.KitchenDataBase

class DishTypeRepository(application: Application) {

    private val dishTypeDao: DishTypeDao = KitchenDataBase.getDataBase(application).dishTypesDao()

    fun getDishTypesForKitchen(kitchenId: Int) =
        dishTypeDao.getDishTypesForKitchen(kitchenId)
}