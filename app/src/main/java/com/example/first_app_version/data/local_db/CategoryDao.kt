package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.first_app_version.data.models.Category

@Dao
interface CategoryDao {

    // Inserts a list of categories into the DB, replacing existing ones on conflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    // Retrieves all categories from the database as an observable LiveData
    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    // Deletes all records from the categories table
    @Query("DELETE FROM categories")
    suspend fun clearAll()
}
