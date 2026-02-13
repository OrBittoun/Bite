package com.example.first_app_version

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.first_app_version.data.repository.UserRepository
import com.example.first_app_version.ui.LoggedInUserViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val loggedInUserViewModel: LoggedInUserViewModel by viewModels()
    private val userRepository = UserRepository()

    private lateinit var navController: NavController
    private lateinit var userStatusIconGlobal: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userStatusIconGlobal = findViewById(R.id.userStatusIconGlobal)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 1. האזנה לשינויים ב-ViewModel ועדכון האייקון בהתאם
        observeUserStatus()

        // 2. ניסיון טעינה אקטיבי של נתוני Firestore אם יש חיבור קיים (סעיף 4)
        loadUserIfAlreadyLoggedIn()
    }

    private fun observeUserStatus() {
        loggedInUserViewModel.user.observe(this) { user ->
            if (user != null) {
                // המשתמש מחובר ונתוניו (כולל שם מה-Firestore) נטענו בהצלחה
                userStatusIconGlobal.setImageResource(R.drawable.user)
                userStatusIconGlobal.setOnClickListener { view ->
                    showUserActionMenu(view)
                }
            } else {
                // אין נתוני משתמש (או שנמחק/התנתק)
                updateIconForLoggedOut(navController.currentDestination?.id)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (loggedInUserViewModel.user.value == null) {
                updateIconForLoggedOut(destination.id)
            }
        }
    }

    private fun updateIconForLoggedOut(destinationId: Int?) {
        if (destinationId == R.id.loginFragment || destinationId == R.id.registerFragment) {
            userStatusIconGlobal.setImageResource(R.drawable.home)
            userStatusIconGlobal.setOnClickListener {
                navController.navigate(R.id.homePageFragment)
            }
        } else {
            userStatusIconGlobal.setImageResource(R.drawable.user)
            userStatusIconGlobal.setOnClickListener { view ->
                showAuthMenu(view)
            }
        }
    }

    private fun showUserActionMenu(view: View) {
        val popup = PopupMenu(this, view)
        val user = loggedInUserViewModel.user.value

        // הצגת השם המלא מה-Firestore כפי שביקשת
        val fullName = "${user?.firstName} ${user?.lastName}".trim()
        popup.menu.add("Hi, $fullName")
        popup.menu.add("Sign out")

        popup.setOnMenuItemClickListener { item ->
            if (item.title == "Sign out") {
                showSignOutDialog()
            }
            true
        }
        popup.show()
    }

    private fun showAuthMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menu.add("Login")
        popup.menu.add("Register")

        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Login" -> navController.navigate(R.id.loginFragment)
                "Register" -> navController.navigate(R.id.registerFragment)
            }
            true
        }
        popup.show()
    }

    private fun showSignOutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Sign out")
            .setMessage("Do you want to sign out?")
            .setPositiveButton("Sign out") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                loggedInUserViewModel.clear()
                navController.navigate(R.id.loginFragment)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // יישום סעיף 4: סנכרון בין Authentication ל-Firestore בטעינה ראשונית
    private fun loadUserIfAlreadyLoggedIn() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            loggedInUserViewModel.clear()
            return
        }

        // כאן אנחנו הולכים ל-Firestore (UserRepository) כדי להביא את השם
        userRepository.getUser(uid) { user, error ->
            if (user != null) {
                loggedInUserViewModel.setUser(user)
            } else {
                // אם המשתמש קיים ב-Auth אבל לא ב-Firestore (כמו שהיה לך קודם)
                loggedInUserViewModel.clear()
                if (error != null) {
                    Toast.makeText(this, "Error loading profile: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}