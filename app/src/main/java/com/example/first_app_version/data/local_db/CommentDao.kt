package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.first_app_version.data.models.Comment

@Dao
interface CommentDao {

    // Upsert thanks to the unique index on (dish_id, author_name)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(comment: Comment)

    @Query("SELECT * FROM comments WHERE dish_id = :dishId AND author_name = :authorName LIMIT 1")
    fun getMyCommentForDish(dishId: Int, authorName: String = "You"): LiveData<Comment?>

    @Query("SELECT * FROM comments WHERE dish_id = :dishId ORDER BY id DESC")
    fun getCommentsForDish(dishId: Int): LiveData<List<Comment>>

    @Query("UPDATE comments SET upvotes = upvotes + 1 WHERE id = :commentId")
    suspend fun incrementUpvotes(commentId: Int)
}