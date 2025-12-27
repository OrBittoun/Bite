package com.example.first_app_version.ui.add_comment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    // Track whether the user has edited to avoid overwriting edits with prefill
    private var userEdited = false
    private var existingCommentPresent = false

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

        // Configure RatingBar as integer 1–5
        binding.ratingBar.numStars = 5
        binding.ratingBar.stepSize = 1f

        // Bind UI to ViewModel draft state
        addCommentViewModel.draftRating.observe(viewLifecycleOwner) { rating ->
            if (!userEdited) {
                binding.ratingBar.rating = (rating ?: 3).toFloat()
            }
        }
        addCommentViewModel.draftText.observe(viewLifecycleOwner) { text ->
            if (!userEdited) {
                val current = binding.commentEditText.text?.toString() ?: ""
                if (current != text) {
                    binding.commentEditText.setText(text ?: "")
                    // Move cursor to end
                    binding.commentEditText.setSelection(binding.commentEditText.text?.length ?: 0)
                }
            }
        }

        // Update ViewModel draft when user edits
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                userEdited = true
                addCommentViewModel.setDraftRating(rating.toInt())
            }
        }
        binding.commentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userEdited = true
                addCommentViewModel.setDraftText(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Observe selected dish ID, then fetch and display the dish + my comment
        selectionViewModel.selectedDishId.observe(viewLifecycleOwner) { dishId ->
            // Fetch and show dish details by ID
            addCommentViewModel.observeDish(dishId).observe(viewLifecycleOwner) { dish ->
                // dish comes from Room via LiveData<Dish>
                binding.dishTitle.text = dish.name
                binding.dishDesc.text = dish.description ?: ""
            }

            // Observe my existing comment for this dish ID and prefill draft only if empty
            addCommentViewModel.observeMyComment(dishId).observe(viewLifecycleOwner) { myComment ->
                existingCommentPresent = myComment != null
                if (addCommentViewModel.isDraftEmpty()) {
                    // Only prefill if user hasn’t started editing
                    addCommentViewModel.prefillDraftFromExisting(myComment)
                    userEdited = false
                }
            }

            binding.submitButton.setOnClickListener {
                val isUpdate = existingCommentPresent
                val title = if (isUpdate) "Update comment?" else "Add comment?"
                val message = if (isUpdate) {
                    "You already commented on this dish. Do you want to update your comment?"
                } else {
                    "Are you sure you want to add this comment?"
                }
                val positiveText = if (isUpdate) "Update" else "Add"

                AlertDialog.Builder(requireContext())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positiveText) { _, _ ->
                        val ratingInt = binding.ratingBar.rating.toInt().coerceIn(1, 5)
                        val text = binding.commentEditText.text?.toString()?.trim().orEmpty()
                        if (text.isEmpty()) {
                            Toast.makeText(requireContext(), "Please write a comment", Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }
                        lifecycleScope.launch {
                            addCommentViewModel.saveMyComment(dishId, ratingInt, text)
                            Toast.makeText(
                                requireContext(),
                                if (isUpdate) "Comment updated" else "Comment added",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}