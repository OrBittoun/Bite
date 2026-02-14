package com.example.first_app_version.ui.authentication

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.first_app_version.R
import com.example.first_app_version.databinding.RegisterLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: RegisterLayoutBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private val loggedInUserViewModel: LoggedInUserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

            if (valid) {
                authViewModel.register(email, password, first, last)
            }
        }

        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.signUpButton.isEnabled = false
                }
                is AuthState.Success -> {
                    binding.signUpButton.isEnabled = true
                    loggedInUserViewModel.setUser(state.user)
                    Toast.makeText(requireContext(), getString(R.string.welcome_message), Toast.LENGTH_SHORT).show()
                    authViewModel.resetState()
                    findNavController().popBackStack()
                }
                is AuthState.Error -> {
                    binding.signUpButton.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    authViewModel.resetState()
                }
                is AuthState.Idle -> {
                    binding.signUpButton.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}