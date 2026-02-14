package com.example.first_app_version.ui.authentication

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.first_app_version.R
import com.example.first_app_version.data.models.User
import com.example.first_app_version.data.repository.AuthRepository
import com.example.first_app_version.data.repository.UserRepository
import com.example.first_app_version.databinding.RegisterLayoutBinding
import com.example.first_app_version.ui.LoggedInUserViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private var _binding: RegisterLayoutBinding? = null
    private val binding get() = _binding!!

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val loggedInUserViewModel: LoggedInUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegisterLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linkToSignin.paintFlags = binding.linkToSignin.paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG

        binding.linkToSignin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.signUpButton.setOnClickListener {
            val first = binding.firstNameEditSignup.text.toString().trim()
            val last = binding.lastNameEditSignup.text.toString().trim()
            val email = binding.emailEditSignup.text.toString().trim()
            val password = binding.passwordEditSignup.text.toString().trim()

            // 1. בדיקות תקינות קלט
            var valid = true
            if (first.isEmpty()) {
                binding.firstNameEditSignup.error = "Required"
                valid = false
            }
            if (last.isEmpty()) {
                binding.lastNameEditSignup.error = "Required"
                valid = false
            }
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditSignup.error = "Please enter a valid email address"
                valid = false
            }
            if (password.length < 6) {
                binding.passwordEditSignup.error = "Password must be at least 6 characters"
                valid = false
            }

            if (!valid) return@setOnClickListener

            // 2. נטרול הכפתור למניעת לחיצות כפולות
            binding.signUpButton.isEnabled = false

            // 3. תהליך הרשמה ב-Firebase Auth
            authRepository.signUp(email, password) { success, error ->
                if (!success) {
                    binding.signUpButton.isEnabled = true
                    Toast.makeText(requireContext(), error ?: "Registration failed", Toast.LENGTH_LONG).show()
                    return@signUp
                }

                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid == null) {
                    binding.signUpButton.isEnabled = true
                    return@signUp
                }

                val user = User(uid = uid, email = email, firstName = first, lastName = last)

                // 4. שמירת המשתמש ב-Firestore
                // ... שאר ה-Imports ...

                userRepository.getUser(uid) { user, err ->
                    binding.signUpButton.isEnabled = true

                    if (user != null) {
                        // עדכון ה-ViewModel בנתוני המשתמש שנטענו מה-Firestore
                        loggedInUserViewModel.setUser(user)

                        Toast.makeText(requireContext(), "Welcome back!", Toast.LENGTH_SHORT).show()

                        // ניסיון לחזור למסך הקודם (למשל AddCommentFragment)
                        val navigatedBack = findNavController().popBackStack()

                        // אם אין מסך קודם לחזור אליו (למשל המשתמש הגיע ישירות ללוגין), עוברים לדף הבית
                        if (!navigatedBack) {
                            findNavController().navigate(R.id.action_loginFragment_to_homePageFragment)
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            err ?: "User data not found",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    // ה-onDestroyView חייב להיות כאן, מחוץ ל-onViewCreated!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}