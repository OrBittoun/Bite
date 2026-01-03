package com.example.first_app_version.data.repository

import android.app.Application
import com.example.first_app_version.data.local_db.CommentDao
import com.example.first_app_version.data.local_db.KitchenDataBase
import com.example.first_app_version.data.models.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommentRepository(application: Application) {

    private val commentDao: CommentDao =
        KitchenDataBase.getDataBase(application).commentsDao()

    fun observeMyComment(dishId: Int, authorName: String = "You") =
        commentDao.getMyCommentForDish(dishId, authorName)

    fun observeComments(dishId: Int) =
        commentDao.getCommentsForDish(dishId)

    suspend fun saveMyComment(
        dishId: Int,
        rating: Int,
        text: String,
        authorName: String = "You"
    ) = withContext(Dispatchers.IO) {
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

    suspend fun deleteMyComment(dishId: Int, authorName: String = "You") =
        withContext(Dispatchers.IO) {
            commentDao.deleteByDishAndAuthorAndSync(dishId, authorName)
        }
}