package com.example.first_app_version.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.first_app_version.R
import com.example.first_app_version.data.repository.AuthRepository
import com.example.first_app_version.data.repository.UserRepository
import com.example.first_app_version.databinding.LoginLayoutBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: LoginLayoutBinding? = null
    private val binding get() = _binding!!

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linkToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {

            val email = binding.emailEditSignin.text.toString().trim()
            val password = binding.passwordEditSignin.text.toString().trim()

            var valid = true

            if (email.isEmpty()) {
                binding.emailEditSignin.error = "Required"
                valid = false
            }

            if (password.isEmpty()) {
                binding.passwordEditSignin.error = "Required"
                valid = false
            }

            if (!valid) return@setOnClickListener

            authRepository.signIn(email, password) { success, error ->

                if (!success) {
                    Toast.makeText(
                        requireContext(),
                        error ?: "Login failed",
                        Toast.LENGTH_LONG
                    ).show()
                    return@signIn
                }

                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid == null) {
                    Toast.makeText(requireContext(), "No user id", Toast.LENGTH_LONG).show()
                    return@signIn
                }

                userRepository.getUser(uid) { user, err ->
                    if (user != null) {
                        findNavController().navigate(R.id.action_loginFragment_to_homePageFragment)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
