
package com.example.first_app_version.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
//new import
import androidx.room.Transaction
import com.example.first_app_version.data.models.Comment

@Dao
interface CommentDao {

    @Query("""
        SELECT * FROM comments
        WHERE dish_id = :dishId AND author_name = :authorName
        LIMIT 1
    """)
    fun getMyCommentForDish(dishId: Int, authorName: String = "You"): LiveData<Comment?>

    @Query("""
        SELECT * FROM comments
        WHERE dish_id = :dishId
        ORDER BY id DESC
    """)
    fun getCommentsForDish(dishId: Int): LiveData<List<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(comment: Comment)

    @Query("UPDATE comments SET upvotes = upvotes + 1 WHERE id = :commentId")
    suspend fun incrementUpvotes(commentId: Int)

    @Query("SELECT COUNT(*) FROM comments WHERE dish_id = :dishId")
    suspend fun countCommentsForDish(dishId: Int): Int

    @Query("UPDATE dishes SET reviews_count = :newCount WHERE id = :dishId")
    suspend fun setDishReviewsCount(dishId: Int, newCount: Int)

    @Transaction
    suspend fun insertOrReplaceAndSyncDishCount(comment: Comment) {
        insertOrReplace(comment)
        val newCount = countCommentsForDish(comment.dishId)
        setDishReviewsCount(comment.dishId, newCount)
    }

    // Delete user’s comment for a dish
    @Query("DELETE FROM comments WHERE dish_id = :dishId AND author_name = :authorName")
    suspend fun deleteByDishAndAuthor(dishId: Int, authorName: String = "You"): Int

    // Transactional delete + reviews_count sync
    @Transaction
    suspend fun deleteByDishAndAuthorAndSync(dishId: Int, authorName: String = "You") {
        deleteByDishAndAuthor(dishId, authorName)
        val newCount = countCommentsForDish(dishId)
        setDishReviewsCount(dishId, newCount)
    }
}
