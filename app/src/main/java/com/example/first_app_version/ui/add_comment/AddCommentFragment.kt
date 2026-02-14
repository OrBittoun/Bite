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
import com.airbnb.lottie.LottieAnimationView
import com.example.first_app_version.R
import com.example.first_app_version.databinding.AddCommentLayoutBinding
import com.example.first_app_version.ui.SelectionViewModel
import kotlinx.coroutines.launch

class AddCommentFragment : Fragment() {

    private var _binding: AddCommentLayoutBinding? = null
    private val binding get() = _binding!!

    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private val addCommentViewModel: AddCommentViewModel by viewModels()

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

        binding.ratingBar.numStars = 5
        binding.ratingBar.stepSize = 1f

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
                    binding.commentEditText.setSelection(
                        binding.commentEditText.text?.length ?: 0
                    )
                }
            }
        }

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

        selectionViewModel.selectedDishId.observe(viewLifecycleOwner) { dishId ->

            addCommentViewModel.observeDish(dishId)
                .observe(viewLifecycleOwner) { dish ->
                    binding.dishTitle.text = dish.name
                    binding.dishDesc.text = dish.description ?: ""
                }

            addCommentViewModel.observeMyComment(dishId)
                .observe(viewLifecycleOwner) { myComment ->
                    existingCommentPresent = myComment != null
                    if (addCommentViewModel.isDraftEmpty()) {
                        addCommentViewModel.prefillDraftFromExisting(myComment)
                        userEdited = false
                    }
                }

            binding.submitButton.setOnClickListener {
                // בדיקה האם המשתמש מחובר ל-Firebase
                val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

                if (currentUser == null) {
                    // המשתמש לא מחובר - מציגים דיאלוג שמפנה להתחברות
                    showLoginRequiredDialog()
                } else {
                    // המשתמש מחובר - ממשיכים בתהליך הרגיל של הדיאלוג שכתבת
                    val id = selectionViewModel.selectedDishId.value ?: return@setOnClickListener
                    showConfirmDialog(id)
                }
            }
        }
    }

    private fun showConfirmDialog(dishId: Int) {
        val isUpdate = existingCommentPresent
        val title = if (isUpdate) "Update comment?" else "Add comment?"
        val message = if (isUpdate) {
            "You already commented on this dish. Do you want to update your comment?"
        } else {
            "Are you sure you want to add this comment?"
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(if (isUpdate) "Update" else "Add", null)
            .setNegativeButton("Cancel") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

                val rating = binding.ratingBar.rating.toInt().coerceIn(1, 5)
                val text = binding.commentEditText.text.toString().trim()

                Toast.makeText(
                    requireContext(),
                    if (isUpdate) "Comment updated" else "Comment added",
                    Toast.LENGTH_SHORT
                ).show()

                lifecycleScope.launch {
                    addCommentViewModel.saveMyComment(dishId, rating, text)
                    dialog.dismiss()
                    showSuccessDialog()
                }
            }
        }

        dialog.show()
    }

    private fun showSuccessDialog() {
        val view = layoutInflater.inflate(R.layout.lottie_dialog, null)
        val lottie = view.findViewById<LottieAnimationView>(R.id.lottieSuccess)

        val dialog =
            android.app.Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar)

        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()

        lottie.playAnimation()

        lottie.postDelayed({
            if (isAdded && dialog.isShowing) {
                dialog.dismiss()
                findNavController().popBackStack()
            }
        }, 4000)
    }

    private fun showLoginRequiredDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Login Required")
            .setMessage("You must be logged in to add a comment. After logging in, you will be returned here.")
            .setPositiveButton("Login") { _, _ ->
                // ניווט פשוט - משאיר את AddCommentFragment ב-Back Stack
                findNavController().navigate(R.id.loginFragment)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
