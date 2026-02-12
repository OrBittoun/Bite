package com.example.first_app_version.ui.dish_display

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.first_app_version.data.models.Comment
import com.example.first_app_version.data.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val repository: CommentRepository
) : ViewModel() {

    fun commentsForDish(dishId: Int): LiveData<List<Comment>> =
        repository.observeComments(dishId)
}