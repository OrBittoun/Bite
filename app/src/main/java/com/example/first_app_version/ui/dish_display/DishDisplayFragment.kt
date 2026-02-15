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
import com.bumptech.glide.Glide
import com.example.first_app_version.R
import com.example.first_app_version.databinding.DishDisplayLayoutBinding
import com.example.first_app_version.ui.all_kitchens.SelectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DishDisplayFragment : Fragment() {

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

        binding.recyclerDishComments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDishComments.adapter = commentAdapter

        selectionViewModel.selectedDishId.observe(viewLifecycleOwner) { dishId ->
            if (dishId == null) return@observe

            currentDishId = dishId

            dishDetailsViewModel.dishById(dishId).observe(viewLifecycleOwner) { dish ->
                if (dish == null) return@observe

                val ctx = requireContext()
                binding.dishTitle.text = dish.name
                binding.dishRestaurant.text = dish.restaurantName
                val img = dish.imageRes ?: R.mipmap.pizza_foreground

                Glide.with(requireContext())
                    .load(img)
                    .placeholder(R.drawable.default_dish)
                    .into(binding.dishImg)

                binding.dishPrice.text =
                    ctx.getString(R.string.dish_price_display, dish.price.toDouble())

                // --- לוגיקת המועדפים של השותף ---
                val heartIcon = if (dish.isFavorite) {
                    R.drawable.ic_favorite_filled
                } else {
                    R.drawable.ic_favorite_border
                }

                binding.favoriteHeart?.setImageResource(heartIcon)
                binding.favoriteHeart?.contentDescription = if (dish.isFavorite)
                    getString(R.string.remove_from_favorites)
                else
                    getString(R.string.add_to_favorites)

                binding.favoriteHeart?.setOnClickListener {
                    dishDetailsViewModel.toggleFavorite(dish.id, !dish.isFavorite)
                }
            }

            commentsViewModel.commentsForDish(dishId).observe(viewLifecycleOwner) { comments ->
                commentAdapter.submitList(comments)
            }

            // --- הלוגיקה שלך לבדיקת תגובה קיימת (שחזרנו אותה!) ---
            commentsViewModel.myCommentForDish(dishId).observe(viewLifecycleOwner) { myComment ->
                if (myComment != null) {
                    binding.addComment.setText(R.string.edit_comment)
                } else {
                    binding.addComment.setText(R.string.add_comment)
                }
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