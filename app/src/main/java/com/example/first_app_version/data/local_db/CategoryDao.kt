package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.first_app_version.data.models.Category


@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //if there is a conflict, replace the old one
    suspend fun insertAll(categories: List<Category>)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("DELETE FROM categories")
    suspend fun clearAll()
}
