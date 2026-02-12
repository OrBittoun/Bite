package com.example.first_app_version.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.first_app_version.R
import com.example.first_app_version.databinding.RegisterLayoutBinding

class RegisterFragment : Fragment() {

    private var _binding: RegisterLayoutBinding? = null
    private val binding get() = _binding!!

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
                binding.firstNameEditSignup.error = "Required"
                valid = false
            }

            if (last.isEmpty()) {
                binding.lastNameEditSignup.error = "Required"
                valid = false
            }

            if (email.isEmpty()) {
                binding.emailEditSignup.error = "Required"
                valid = false
            }

            if (password.isEmpty()) {
                binding.passwordEditSignup.error = "Required"
                valid = false
            }

            if (!valid) return@setOnClickListener

            // כאן נחבר Firebase בהמשך
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
