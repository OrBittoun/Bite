package com.example.first_app_version.data.repository

import android.app.Application
import com.example.first_app_version.data.local_db.KitchenDao
import com.example.first_app_version.data.local_db.KitchenDataBase
import com.example.first_app_version.data.models.Kitchen

class KitchenRepository(application: Application) {

    private val kitchenDao: KitchenDao = KitchenDataBase.getDataBase(application.applicationContext).kitchensDao()

    fun getItems() = kitchenDao.getKitchens()
    fun getKitchen(id: Int): Kitchen? = try { kitchenDao.getKitchen(id) } catch (_: Exception) { null }

}