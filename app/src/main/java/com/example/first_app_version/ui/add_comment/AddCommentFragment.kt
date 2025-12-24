package com.example.first_app_version.ui.add_comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.first_app_version.databinding.AddCommentLayoutBinding
import com.example.first_app_version.ui.SelectionViewModel
import kotlinx.coroutines.launch

class AddCommentFragment : Fragment() {

    private var _binding: AddCommentLayoutBinding? = null
    private val binding get() = _binding!!

    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private val addCommentViewModel: AddCommentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddCommentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show selected dish title and description
        selectionViewModel.selectedDish.observe(viewLifecycleOwner) { dish ->
            binding.dishTitle.text = dish.name
            binding.dishDesc.text = dish.description ?: ""
            // Prefill existing comment for "You", if any
            addCommentViewModel.observeMyComment(dish.id).observe(viewLifecycleOwner) { myComment ->
                if (myComment != null) {
                    binding.ratingBar.rating = myComment.rating.toFloat()
                    binding.commentEditText.setText(myComment.text)
                } else {
                    binding.ratingBar.rating = 3f // default
                    binding.commentEditText.setText("")
                }
            }

            binding.submitButton.setOnClickListener {
                val ratingInt = binding.ratingBar.rating.toInt().coerceIn(1, 5)
                val text = binding.commentEditText.text?.toString()?.trim().orEmpty()
                if (text.isEmpty()) {
                    Toast.makeText(requireContext(), "Please write a comment", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                lifecycleScope.launch {
                    addCommentViewModel.saveMyComment(dish.id, ratingInt, text)
                    Toast.makeText(requireContext(), "Comment saved", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack() // go back to dish display
                }
            }
        }

        // Configure RatingBar as integer 1–5
        binding.ratingBar.numStars = 5
        binding.ratingBar.stepSize = 1f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}