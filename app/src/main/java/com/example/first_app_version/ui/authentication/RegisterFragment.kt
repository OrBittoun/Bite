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
import com.example.first_app_version.data.repository.AuthRepository
import com.example.first_app_version.data.repository.UserRepository
import com.example.first_app_version.databinding.RegisterLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.first_app_version.data.models.User
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


        binding.linkToSignin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.signUpButton.setOnClickListener {
            val first = binding.firstNameEditSignup.text.toString().trim()
            val last = binding.lastNameEditSignup.text.toString().trim()
            val email = binding.emailEditSignup.text.toString().trim()
            val password = binding.passwordEditSignup.text.toString().trim()

            var valid = true
            if (first.isEmpty()) {
                binding.firstNameEditSignup.error = getString(R.string.error_required)
                valid = false
            }
            if (last.isEmpty()) {
                binding.lastNameEditSignup.error = getString(R.string.error_required)
                valid = false
            }
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEditSignup.error = getString(R.string.error_invalid_email)
                valid = false
            }
            if (password.length < 6) {
                binding.passwordEditSignup.error = getString(R.string.error_password_short)
                valid = false
            }

            if (!valid) return@setOnClickListener

            binding.signUpButton.isEnabled = false

            authRepository.signUp(email, password) { success, _ ->
                if (!success) {
                    binding.signUpButton.isEnabled = true
                    Toast.makeText(requireContext(), getString(R.string.registration_failed), Toast.LENGTH_LONG).show()
                    return@signUp
                }

                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@signUp

                val newUser = User(
                    uid = uid,
                    email = email,
                    firstName = first,
                    lastName = last
                )
                userRepository.saveUser(newUser) { isSaved, dbError ->
                    binding.signUpButton.isEnabled = true
                    if (isSaved) {
                        loggedInUserViewModel.setUser(newUser)
                        Toast.makeText(requireContext(), getString(R.string.welcome_message), Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        android.util.Log.e("RegisterFragment", "Database error: $dbError")
                        Toast.makeText(requireContext(), getString(R.string.registration_failed), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}