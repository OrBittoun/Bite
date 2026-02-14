package com.example.first_app_version.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.first_app_version.R
import com.example.first_app_version.databinding.LoginLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: LoginLayoutBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private val loggedInUserViewModel: LoggedInUserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
                binding.emailEditSignin.error = getString(R.string.error_required)
                valid = false
            }
            if (password.isEmpty()) {
                binding.passwordEditSignin.error = getString(R.string.error_required)
                valid = false
            }

            if (valid) {
                authViewModel.login(email, password)
            }
        }

        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.loginButton.isEnabled = false
                }
                is AuthState.Success -> {
                    binding.loginButton.isEnabled = true
                    loggedInUserViewModel.setUser(state.user)
                    Toast.makeText(requireContext(), getString(R.string.welcome_back), Toast.LENGTH_SHORT).show()
                    authViewModel.resetState() // מונע קריאה כפולה

                    val navigatedBack = findNavController().popBackStack()
                    if (!navigatedBack) {
                        findNavController().navigate(R.id.action_loginFragment_to_homePageFragment)
                    }
                }
                is AuthState.Error -> {
                    binding.loginButton.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    authViewModel.resetState()
                }
                is AuthState.Idle -> {
                    binding.loginButton.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}