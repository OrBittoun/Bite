package com.example.first_app_version.ui.dish_display

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.first_app_version.data.models.Comment
import com.example.first_app_version.data.repository.CommentRepository

class CommentsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CommentRepository(application)

    fun commentsForDish(dishId: Int): LiveData<List<Comment>> =
        repository.observeComments(dishId)
}