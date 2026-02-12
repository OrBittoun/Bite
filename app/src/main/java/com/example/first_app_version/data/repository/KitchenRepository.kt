package com.example.first_app_version.data.repository

import androidx.lifecycle.LiveData
import com.example.first_app_version.data.local_db.KitchenDao
import com.example.first_app_version.data.models.Kitchen
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KitchenRepository @Inject constructor(
    private val kitchenDao: KitchenDao
) {
    fun getItems(): LiveData<List<Kitchen>> = kitchenDao.getKitchens()

    suspend fun getKitchen(id: Int): Kitchen? =
        try {
            kitchenDao.getKitchen(id)
        } catch (_: Exception) {
            null
        }
}