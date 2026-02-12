package com.example.first_app_version.data.repository

import androidx.lifecycle.LiveData
import com.example.first_app_version.data.local_db.CommentDao
import com.example.first_app_version.data.models.Comment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val commentDao: CommentDao
) {
    fun observeMyComment(dishId: Int, authorName: String = "You"): LiveData<Comment?> =
        commentDao.getMyCommentForDish(dishId, authorName)

    fun observeComments(dishId: Int): LiveData<List<Comment>> =
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