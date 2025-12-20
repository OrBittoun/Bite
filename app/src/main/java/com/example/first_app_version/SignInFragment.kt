package com.example.first_app_version

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.first_app_version.databinding.SignInLayoutBinding

class SignInFragment : Fragment() {

    private var _binding : SignInLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = SignInLayoutBinding.inflate(inflater, container, false)

        binding.login.setOnClickListener {
            // Need to add login logic and validation
            // Should include bundle or something with the details of the user
            //findNavController().navigate(R.id.action_signInFragment_to_homePageFragment)
        }

        binding.signUp.setOnClickListener {
            //findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}