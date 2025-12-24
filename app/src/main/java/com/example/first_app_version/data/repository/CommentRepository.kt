package com.example.first_app_version.data.repository

import android.app.Application
import com.example.first_app_version.data.local_db.CommentDao
import com.example.first_app_version.data.local_db.KitchenDataBase
import com.example.first_app_version.data.models.Comment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentRepository(application: Application) {

    private val commentDao: CommentDao = KitchenDataBase.getDataBase(application).commentsDao()

    fun getCommentsForDish(dishId: Int) =
        commentDao.getCommentsForDish(dishId)

    suspend fun addComment(comment: Comment) =
        commentDao.insert(comment)

    suspend fun deleteComment(comment: Comment) =
        commentDao.delete(comment)

    suspend fun upvoteComment(commentId: Int) =
        commentDao.incrementUpvotes(commentId)

    // Helper to create a Comment with a properly formatted createdAt string.
    // Usage: val c = repo.newComment(dishId, rating, text); repo.addComment(c)
    fun newComment(dishId: Int, rating: Int, text: String, authorName: String = "You"): Comment {
        val formatter = SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault())
        val timestamp = formatter.format(Date())
        return Comment(
            dishId = dishId,
            authorName = authorName,
            rating = rating,
            text = text,
            createdAt = timestamp,
            upvotes = 0
        )
    }
}