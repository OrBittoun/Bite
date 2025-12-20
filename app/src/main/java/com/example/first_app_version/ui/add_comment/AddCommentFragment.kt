package com.example.first_app_version.ui.add_comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.first_app_version.R
import com.example.first_app_version.databinding.NewCommentLayoutBinding

class AddCommentFragment : Fragment() {
    private var _binding : NewCommentLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = NewCommentLayoutBinding.inflate(inflater, container, false)

        binding.addComment.setOnClickListener {
            // Fix logic
            Toast.makeText(requireContext(), "Should add comment", Toast.LENGTH_SHORT ).show()
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