package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.first_app_version.data.models.Comment

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: Comment)

    @Delete
    suspend fun delete(comment: Comment)

    // Using id DESC as a proxy for "newest first" since createdAt is a string.
    // If you later store epoch millis, you can ORDER BY created_at_millis DESC.
    @Query("SELECT * FROM comments WHERE dish_id = :dishId ORDER BY id DESC")
    fun getCommentsForDish(dishId: Int): LiveData<List<Comment>>

    @Query("UPDATE comments SET upvotes = upvotes + 1 WHERE id = :commentId")
    suspend fun incrementUpvotes(commentId: Int)
}