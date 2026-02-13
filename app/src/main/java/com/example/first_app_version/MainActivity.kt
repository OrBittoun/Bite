package com.example.first_app_version

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.first_app_version.data.repository.UserRepository
import com.example.first_app_version.ui.LoggedInUserViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val loggedInUserViewModel: LoggedInUserViewModel by viewModels()
    private val userRepository = UserRepository()

    private lateinit var navController: NavController
    private lateinit var userStatusTextGlobal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userStatusTextGlobal = findViewById(R.id.userStatusTextGlobal)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        observeUserStatus()
        loadUserIfAlreadyLoggedIn() // קריאה לפונקציה שמוודאת אם המשתמש כבר מחובר
    }

    private fun observeUserStatus() {
        // האזנה לשינויים ב-ViewModel (פותר את בעיית השם שלא מתעדכן)
        loggedInUserViewModel.user.observe(this) { user ->
            if (user != null) {
                val fullName = "${user.firstName} ${user.lastName}".trim()
                val displayName = if (fullName.isBlank()) user.email else fullName
                userStatusTextGlobal.text = "Hi, $displayName"

                userStatusTextGlobal.setOnClickListener {
                    showSignOutDialog() // קריאה לפונקציה שהייתה חסרה לך
                }
            } else {
                updateUIForLoggedOut(navController.currentDestination?.id)
            }
        }

        // האזנה לשינויי מסכים (Navigation)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (loggedInUserViewModel.user.value == null) {
                updateUIForLoggedOut(destination.id)
            }
        }
    }

    // פתרון לשגיאה 2: הגדרת הדיאלוג של התנתקות
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

    // לוגיקה למצב שאין משתמש מחובר
    private fun updateUIForLoggedOut(destinationId: Int?) {
        if (destinationId == R.id.loginFragment || destinationId == R.id.registerFragment) {
            userStatusTextGlobal.text = "Home"
            userStatusTextGlobal.setOnClickListener {
                navController.navigate(R.id.homePageFragment)
            }
        } else {
            userStatusTextGlobal.text = "LogIn"
            userStatusTextGlobal.setOnClickListener {
                navController.navigate(R.id.loginFragment)
            }
        }
    }

    // פתרון לשגיאה 1: החזרת הפונקציה שטוענת את המשתמש מה-Firestore בהפעלה
    private fun loadUserIfAlreadyLoggedIn() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            loggedInUserViewModel.clear()
            return
        }

        userRepository.getUser(uid) { user, _ ->
            loggedInUserViewModel.setUser(user)
        }
    }
}