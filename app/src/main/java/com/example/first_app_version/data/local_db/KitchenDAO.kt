package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.first_app_version.data.models.Kitchen

@Dao
interface KitchenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem (kitchen: Kitchen)

    @Delete
    fun deleteItem (kitchen: Kitchen)

    @Update
    fun updateItem (kitchen: Kitchen)

    @Query("SELECT * FROM kitchens ORDER BY id ASC")
    fun getItems() : LiveData<List<Kitchen>>

    @Query("SELECT * FROM kitchens WHERE id LIKE :id")
    fun getItem(id : Int) : Kitchen

    @Query("DELETE FROM kitchens ")
    fun deleteAll()

}