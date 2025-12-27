package com.example.first_app_version.ui.dish_display

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.databinding.DishDisplayLayoutBinding
import com.example.first_app_version.ui.SelectionViewModel

class DishDisplayPageFragment : Fragment() {

    private var _binding: DishDisplayLayoutBinding? = null
    private val binding get() = _binding!!

    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private val dishDetailsViewModel: DishDetailsViewModel by viewModels()
    private val commentsViewModel: CommentsViewModel by viewModels()

    private val commentAdapter = CommentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DishDisplayLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView once
        binding.recyclerDishComments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDishComments.adapter = commentAdapter
        binding.recyclerDishComments.setHasFixedSize(true)

        // Add bottom system bar inset to RecyclerView padding so last item is fully visible
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerDishComments) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Convert 16dp to px and add system bar bottom
            val extraBottomPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics
            ).toInt()
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, systemBars.bottom + extraBottomPx)
            insets
        }

        // Observe the selected dish ID, then fetch the dish + its comments
        selectionViewModel.selectedDishId.observe(viewLifecycleOwner) { dishId ->
            // Fetch and show dish details by ID
            dishDetailsViewModel.dishById(dishId).observe(viewLifecycleOwner) { dish ->
                binding.dishTitle.text = dish.name
                binding.dishDesc.text = dish.description ?: ""
                val img = dish.imageRes ?: R.mipmap.pizza_foreground
                binding.dishImg.setImageResource(img)
            }

            // Load comments by dish ID
            commentsViewModel.commentsForDish(dishId).observe(viewLifecycleOwner) { comments ->
                commentAdapter.submitList(comments)
            }

            binding.addComment.setOnClickListener {
                findNavController().navigate(R.id.action_dishDisplayPageFragment_to_addCommentFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}