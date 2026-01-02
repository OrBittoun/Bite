package com.example.first_app_version.ui.add_comment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.first_app_version.data.models.Comment
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.repository.CommentRepository
import com.example.first_app_version.data.repository.DishRepository

class AddCommentViewModel(application: Application) : AndroidViewModel(application) {
    private val commentRepository = CommentRepository(application)
    private val dishRepository = DishRepository(application)

    // Draft state that survives onViewCreated
    private val _draftText = MutableLiveData<String>("")
    val draftText: LiveData<String> get() = _draftText

    private val _draftRating = MutableLiveData<Int>(3)
    val draftRating: LiveData<Int> get() = _draftRating

    // Track current dish to optionally reset draft when switching dishes
    private var currentDishId: Int? = null

    // Fetch dish details by ID
    fun observeDish(dishId: Int): LiveData<Dish> {
        return dishRepository.getDishById(dishId)
    }

    fun observeMyComment(dishId: Int): LiveData<Comment?> {
        // If we switched dishes, clear draft to use that dish's existing comment
        if (currentDishId != dishId) {
            currentDishId = dishId
            clearDraft() // To allow using existing comment text (if exists)
        }
        return commentRepository.observeMyComment(dishId)
    }

    suspend fun saveMyComment(dishId: Int, rating: Int, text: String) {
        commentRepository.saveMyComment(dishId, rating, text)
        _draftRating.value = rating
        _draftText.value = text
    }

    suspend fun deleteMyComment(dishId: Int) {
        commentRepository.deleteMyComment(dishId)
        clearDraft()
    }


    fun setDraftText(text: String) {
        _draftText.value = text
    }

    fun setDraftRating(rating: Int) {
        _draftRating.value = rating
    }

    fun prefillDraftFromExisting(comment: Comment?) {
        if (comment != null) {
            _draftRating.value = comment.rating
            _draftText.value = comment.text
        } else {
            clearDraft()
        }
    }

    fun isDraftEmpty(): Boolean {
        val t = _draftText.value
        val r = _draftRating.value
        return (t.isNullOrEmpty() && (r == null || r == 3))
    }

    fun clearDraft() {
        _draftText.value = ""
        _draftRating.value = 3
    }
}