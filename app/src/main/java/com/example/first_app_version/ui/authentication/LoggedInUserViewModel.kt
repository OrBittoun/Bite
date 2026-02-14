package com.example.first_app_version.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.first_app_version.data.models.User

class LoggedInUserViewModel : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun setUser(user: User?) {
        _user.value = user
    }

    fun clear() {
        _user.value = null
    }
}
