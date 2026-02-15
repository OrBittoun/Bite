package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.first_app_version.data.models.DishType

@Dao
interface DishTypeDao {

    // Inserts a list of dish types, replacing any existing records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(types: List<DishType>)

    // Retrieves all dish types belonging to a specific kitchen, sorted alphabetically
    @Query("SELECT * FROM dish_types WHERE kitchen_id = :kitchenId ORDER BY name ASC")
    fun getDishTypesForKitchen(kitchenId: Int): LiveData<List<DishType>>
}