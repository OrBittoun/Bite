package com.example.first_app_version.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.first_app_version.data.models.User
import com.example.first_app_version.data.repository.AuthRepository
import com.example.first_app_version.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun login(email: String, pass: String) {
        _authState.value = AuthState.Loading
        authRepository.signIn(email, pass) { success, msg ->
            if (success) {
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    userRepository.getUser(uid) { user, err ->
                        if (user != null) _authState.value = AuthState.Success(user)
                        else _authState.value = AuthState.Error(err ?: "User not found")
                    }
                } else {
                    _authState.value = AuthState.Error("No user ID")
                }
            } else {
                _authState.value = AuthState.Error(msg ?: "Login failed")
            }
        }
    }

    fun register(email: String, pass: String, first: String, last: String) {
        _authState.value = AuthState.Loading
        authRepository.signUp(email, pass) { success, msg ->
            if (success) {
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    val newUser = User(uid, email, first, last)
                    userRepository.saveUser(newUser) { isSaved, dbError ->
                        if (isSaved) _authState.value = AuthState.Success(newUser)
                        else _authState.value = AuthState.Error(dbError ?: "Registration DB error")
                    }
                } else {
                    _authState.value = AuthState.Error("No user ID")
                }
            } else {
                _authState.value = AuthState.Error(msg ?: "Registration failed")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}