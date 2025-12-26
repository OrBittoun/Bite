package com.example.first_app_version.ui.dish_display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.first_app_version.R
import com.example.first_app_version.databinding.DishDisplayLayoutBinding
import com.example.first_app_version.ui.SelectionViewModel
import com.example.first_app_version.ui.all_dishes.DishesViewModel

class DishDisplayPageFragment : Fragment() {

    private var _binding: DishDisplayLayoutBinding? = null
    private val binding get() = _binding!!

    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private val dishesViewModel: DishesViewModel by viewModels()

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

        // ⭐ מאזינים ל-ID שנבחר
        selectionViewModel.selectedDishId.observe(viewLifecycleOwner) { dishId ->

            // ⭐ מביאים את המנה מה-DB לפי ה-ID
            dishesViewModel.getDishById(dishId).observe(viewLifecycleOwner) { dish ->
                binding.dishTitle.text = dish.name
                binding.dishDesc.text = dish.description ?: ""
                binding.dishImg.setImageResource(
                    dish.imageRes ?: R.drawable.default_dish
                )
            }
        }

        binding.addComment.setOnClickListener {
            findNavController().navigate(
                R.id.action_dishDisplayPageFragment_to_addCommentFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}