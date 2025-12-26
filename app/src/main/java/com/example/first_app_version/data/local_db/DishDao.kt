package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.first_app_version.data.models.Dish

@Dao
interface DishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dishes: List<Dish>)

    @Query("SELECT * FROM dishes WHERE dish_type_id = :dishTypeId ORDER BY name ASC")
    fun getDishesForDishType(dishTypeId: Int): LiveData<List<Dish>>

    @Query("SELECT * FROM dishes WHERE id = :dishId LIMIT 1")
    fun getDishById(dishId: Int): LiveData<Dish>

}