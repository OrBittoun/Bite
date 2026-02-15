package com.example.first_app_version.data.repository

import com.example.first_app_version.data.models.User
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val usersCollection = db.collection("users")

    // Saves a user profile to Firestore and notifies the result via callback
    fun saveUser(user: User, onResult: (Boolean, String?) -> Unit) {
        usersCollection.document(user.uid)
            .set(user)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    // Retrieves a user profile from Firestore based on their unique UID
    fun getUser(uid: String, onResult: (User?, String?) -> Unit) {
        usersCollection.document(uid)
            .get()
            .addOnSuccessListener { doc ->
                val user = doc.toObject(User::class.java)
                onResult(user, null)
            }
            .addOnFailureListener { e ->
                android.util.Log.e("UserRepository", "Error fetching user: ${e.message}")
                onResult(null, e.message)
            }
    }
}