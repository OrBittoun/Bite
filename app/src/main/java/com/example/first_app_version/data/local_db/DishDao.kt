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

    // Inserts a list of dishes into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dishes: List<Dish>)

    // Inserts a single dish (useful for saving API dishes locally)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dish: Dish)

    // Retrieves all dishes of a specific type, ordered alphabetically
    @Query("SELECT * FROM dishes WHERE dish_type_id = :dishTypeId ORDER BY name ASC")
    fun getDishesForDishType(dishTypeId: Int): LiveData<List<Dish>>

    // Fetches a single dish by its unique ID
    @Query("SELECT * FROM dishes WHERE id = :dishId LIMIT 1")
    fun getDishById(dishId: Int): LiveData<Dish>

    // Retrieves only the local image resource ID for a specific dish
    @Query("SELECT image_res FROM dishes WHERE id = :dishId LIMIT 1")
    suspend fun getImageResForDish(dishId: Int): Int?

    // Updates the favorite status (true/false) of a specific dish
    @Query("UPDATE dishes SET is_favorite = :isFavorite WHERE id = :dishId")
    suspend fun updateFavoriteStatus(dishId: Int, isFavorite: Boolean)

    // Retrieves a list of all dishes that are marked as favorite
    @Query("SELECT * FROM dishes WHERE is_favorite = 1")
    fun getFavoriteDishes(): LiveData<List<Dish>>

    // Removes a specific dish from the database
    @Delete
    suspend fun deleteDish(dish: Dish)
}