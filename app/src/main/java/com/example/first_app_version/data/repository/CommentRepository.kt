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

    fun observeMyComment(dishId: Int, authorName: String = "You") =
        commentDao.getMyCommentForDish(dishId, authorName)

    fun observeComments(dishId: Int) =
        commentDao.getCommentsForDish(dishId)

    suspend fun saveMyComment(dishId: Int, rating: Int, text: String, authorName: String = "You") {
        // createdAt at the time of submit
        val formatter = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
        val timestamp = formatter.format(Date())
        val comment = Comment(
            dishId = dishId,
            authorName = authorName,
            rating = rating,
            text = text,
            createdAt = timestamp,
            upvotes = 0 // reset on create/edit
        )
        // Keep dish.reviews_count in sync
        commentDao.insertOrReplaceAndSyncDishCount(comment)
    }

    suspend fun deleteMyComment(dishId: Int, authorName: String = "You") {
        // Delete and sync dish.reviews_count
        commentDao.deleteByDishAndAuthorAndSync(dishId, authorName)
    }

    suspend fun upvote(commentId: Int) = commentDao.incrementUpvotes(commentId)
}