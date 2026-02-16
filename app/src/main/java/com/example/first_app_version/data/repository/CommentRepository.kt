package com.example.first_app_version.data.repository

import androidx.lifecycle.LiveData
import com.example.first_app_version.data.local_db.CommentDao
import com.example.first_app_version.data.models.Comment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val commentDao: CommentDao
) {
    // Observes the current user's comment for a specific dish
    fun observeMyComment(dishId: Int, authorName: String = "You"): LiveData<Comment?> =
        commentDao.getMyCommentForDish(dishId, authorName)

    // Observes all comments for a specific dish
    fun observeComments(dishId: Int): LiveData<List<Comment>> =
        commentDao.getCommentsForDish(dishId)

    // Saves or updates a comment and synchronizes the dish's review count
    suspend fun saveMyComment(
        dishId: Int,
        rating: Int,
        text: String,
        authorName: String = "You"
    ) {
        val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
        val timestamp = formatter.format(Date())

        val comment = Comment(
            dishId = dishId,
            authorName = authorName,
            rating = rating,
            text = text,
            createdAt = timestamp,
        )

        commentDao.insertOrReplaceAndSyncDishCount(comment)
    }

    // Deletes the user's comment and updates the total review count
    suspend fun deleteMyComment(dishId: Int, authorName: String = "You") {
        commentDao.deleteByDishAndAuthorAndSync(dishId, authorName)
    }
}