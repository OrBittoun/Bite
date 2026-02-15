package com.example.first_app_version

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.first_app_version.data.repository.UserRepository
import com.example.first_app_version.ui.authentication.LoggedInUserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val loggedInUserViewModel: LoggedInUserViewModel by viewModels()

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var navController: NavController
    private lateinit var userStatusIconGlobal: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userStatusIconGlobal = findViewById(R.id.userStatusIconGlobal)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        observeUserStatus()
        loadUserIfAlreadyLoggedIn()
    }

    private fun observeUserStatus() {
        loggedInUserViewModel.user.observe(this) { user ->
            if (user != null) {
                userStatusIconGlobal.setImageResource(R.drawable.user)
                userStatusIconGlobal.setOnClickListener { view ->
                    showUserActionMenu(view)
                }
            } else {
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

        val fullName = "${user?.firstName} ${user?.lastName}".trim()

        popup.menu.add(getString(R.string.menu_hi_user, fullName))
        popup.menu.add(getString(R.string.menu_sign_out))

        popup.setOnMenuItemClickListener { item ->
            if (item.title == getString(R.string.menu_sign_out)) {
                showSignOutDialog()
            }
            true
        }
        popup.show()
    }

    private fun showAuthMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menu.add(getString(R.string.menu_login))
        popup.menu.add(getString(R.string.menu_register))

        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                getString(R.string.menu_login) -> navController.navigate(R.id.loginFragment)
                getString(R.string.menu_register) -> navController.navigate(R.id.registerFragment)
            }
            true
        }
        popup.show()
    }

    private fun showSignOutDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_sign_out_title))
            .setMessage(getString(R.string.dialog_sign_out_msg))
            .setPositiveButton(getString(R.string.menu_sign_out)) { _, _ ->
                auth.signOut()
                loggedInUserViewModel.clear()
                navController.navigate(R.id.loginFragment)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun loadUserIfAlreadyLoggedIn() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            loggedInUserViewModel.clear()
            return
        }

        userRepository.getUser(uid) { user, error ->
            if (user != null) {
                loggedInUserViewModel.setUser(user)
            } else {
                loggedInUserViewModel.clear()
                if (error != null) {
                    Toast.makeText(this, getString(R.string.error_loading_profile, error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}