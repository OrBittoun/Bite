package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.first_app_version.data.models.Comment

@Dao
interface CommentDao {

    // Fetches a single comment made by a specific author for a specific dish
    @Query(" SELECT * FROM comments WHERE dish_id = :dishId AND author_name = :authorName LIMIT 1")
    fun getMyCommentForDish(dishId: Int, authorName: String = "You"): LiveData<Comment?>

    // Retrieves all comments for a given dish, ordered from newest to oldest
    @Query("SELECT * FROM comments WHERE dish_id = :dishId ORDER BY id DESC")
    fun getCommentsForDish(dishId: Int): LiveData<List<Comment>>

    // Inserts a new comment or replaces it if a conflict occurs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(comment: Comment)

    // Returns the total number of comments for a specific dish
    @Query("SELECT COUNT(*) FROM comments WHERE dish_id = :dishId")
    suspend fun countCommentsForDish(dishId: Int): Int

    // Updates the total review count for a specific dish
    @Query("UPDATE dishes SET reviews_count = :newCount WHERE id = :dishId")
    suspend fun setDishReviewsCount(dishId: Int, newCount: Int)

    // Safely executes multiple queries: inserts a comment, counts them, and updates the dish's count
    @Transaction
    suspend fun insertOrReplaceAndSyncDishCount(comment: Comment) {
        insertOrReplace(comment)
        val newCount = countCommentsForDish(comment.dishId)
        setDishReviewsCount(comment.dishId, newCount)
    }

    // Deletes a comment and immediately updates the review count for that dish
    @Transaction
    suspend fun deleteByDishAndAuthorAndSync(dishId: Int, authorName: String = "You") {
        deleteByDishAndAuthor(dishId, authorName)
        val newCount = countCommentsForDish(dishId)
        setDishReviewsCount(dishId, newCount)
    }

    // Deletes a specific user's comment for a dish and returns the number of rows deleted
    @Query("DELETE FROM comments WHERE dish_id = :dishId AND author_name = :authorName")
    suspend fun deleteByDishAndAuthor(dishId: Int, authorName: String = "You"): Int
}