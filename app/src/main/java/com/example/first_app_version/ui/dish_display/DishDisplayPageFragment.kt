package com.example.first_app_version.ui.dish_display

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.databinding.DishDisplayLayoutBinding
import com.example.first_app_version.ui.all_kitchens.SelectionViewModel

class DishDisplayPageFragment : Fragment() {

    private var _binding: DishDisplayLayoutBinding? = null
    private val binding get() = _binding!!

    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private val dishDetailsViewModel: DishDetailsViewModel by viewModels()
    private val commentsViewModel: CommentsViewModel by viewModels()

    private val commentAdapter = CommentAdapter()

    private var currentDishId: Int? = null

    companion object {
        const val ARG_DISH_ID = "dish_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DishDisplayLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerDishComments.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerDishComments.adapter = commentAdapter
        binding.recyclerDishComments.setHasFixedSize(true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerDishComments) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val extraBottomPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16f,
                resources.displayMetrics
            ).toInt()

            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                systemBars.bottom + extraBottomPx
            )
            insets
        }

        selectionViewModel.selectedDishId.observe(viewLifecycleOwner) { dishId ->
            val ctx = binding.root.context
            currentDishId = dishId

            dishDetailsViewModel.dishById(dishId).observe(viewLifecycleOwner) { dish ->
                binding.dishTitle.text = dish.name
                binding.dishDesc.text = dish.description ?: ""
                binding.dishRestaurant?.text = dish.restaurantName
                val img = dish.imageRes ?: R.mipmap.pizza_foreground
                binding.dishImg.setImageResource(img)
                binding.dishPrice.text = ctx.getString(R.string.dish_price_display, dish.price.toDouble())
            }

            commentsViewModel.commentsForDish(dishId).observe(viewLifecycleOwner) { comments ->
                commentAdapter.submitList(comments)
            }
        }

        binding.addComment.setOnClickListener {
            val dishId = currentDishId ?: return@setOnClickListener

            findNavController().navigate(
                R.id.action_dishDisplayPageFragment_to_addCommentFragment,
                bundleOf(ARG_DISH_ID to dishId)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
