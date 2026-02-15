package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.first_app_version.data.models.Dish

@Dao
interface DishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dishes: List<Dish>)

    // פונקציה חדשה להוספת מנה בודדת (חיוני לשמירת מנה מה-API למועדפים)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: Dish)

    @Query("SELECT * FROM dishes WHERE dish_type_id = :dishTypeId ORDER BY name ASC")
    fun getDishesForDishType(dishTypeId: Int): LiveData<List<Dish>>

    @Query("SELECT * FROM dishes WHERE id = :dishId LIMIT 1")
    fun getDishById(dishId: Int): LiveData<Dish>

    @Query("SELECT image_res FROM dishes WHERE id = :dishId LIMIT 1")
    suspend fun getImageResForDish(dishId: Int): Int?

    // --- התוספות של השותף עבור מערכת המועדפים ---

    // פונקציה לעדכון הסטטוס של המנה (מועדף או לא)
    @Query("UPDATE dishes SET is_favorite = :isFavorite WHERE id = :dishId")
    suspend fun updateFavoriteStatus(dishId: Int, isFavorite: Boolean)

    // פונקציה לשליפת כל המנות שמוגדרות כמועדפות
    @Query("SELECT * FROM dishes WHERE is_favorite = 1")
    fun getFavoriteDishes(): LiveData<List<Dish>>

    @Delete
    suspend fun deleteDish(dish: Dish)
}