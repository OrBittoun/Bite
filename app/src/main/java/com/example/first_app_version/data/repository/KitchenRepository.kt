package com.example.first_app_version.data.repository

import android.app.Application
import com.example.first_app_version.data.local_db.KitchenDao
import com.example.first_app_version.data.local_db.KitchenDataBase
import com.example.first_app_version.data.models.Kitchen

class KitchenRepository (application: Application) {

    private var kitchenDao : KitchenDao?

    init {
        val db = KitchenDataBase.getDataBase(application.applicationContext)
        kitchenDao = db.kitchensDao()
    }

    fun getItems() = kitchenDao?.getItems()

}