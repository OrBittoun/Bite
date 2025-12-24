package com.example.first_app_version.ui.add_comment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.first_app_version.data.models.Comment
import com.example.first_app_version.data.repository.CommentRepository

class AddCommentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CommentRepository(application)

    fun observeMyComment(dishId: Int): LiveData<Comment?> =
        repository.observeMyComment(dishId)

    suspend fun saveMyComment(dishId: Int, rating: Int, text: String) {
        repository.saveMyComment(dishId, rating, text)
    }
}