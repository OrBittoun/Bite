package com.example.first_app_version.data.repository

import com.example.first_app_version.data.models.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun saveUser(user: User, onResult: (Boolean, String?) -> Unit) {
        usersCollection.document(user.uid)
            .set(user)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    fun getUser(uid: String, onResult: (User?, String?) -> Unit) {
        usersCollection.document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val user = doc.toObject(User::class.java)
                onResult(user, null)
            }
            .addOnFailureListener { e -> onResult(null, e.message) }
    }
}
