package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.first_app_version.data.models.Kitchen

@Dao
interface KitchenDao {

    @Query("SELECT * FROM kitchens ORDER BY id ASC")
    fun getKitchens(): LiveData<List<Kitchen>>

    @Query("SELECT * FROM kitchens WHERE id = :id LIMIT 1")
    fun getKitchen(id: Int): Kitchen

}