package com.example.first_app_version.ui.all_dishes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.databinding.DishesLayoutBinding
import com.example.first_app_version.ui.all_kitchens.SelectionViewModel
import com.example.first_app_version.data.models.Dish
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DishesFragment : Fragment() {

    private var _binding: DishesLayoutBinding? = null
    private val binding get() = _binding!!

    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private val dishesViewModel: DishesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DishesLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // האזנה למצב: האם אנחנו במועדפים או במצב רגיל?
        selectionViewModel.isFavoritesMode.observe(viewLifecycleOwner) { isFavorites ->
            if (isFavorites == true) {
                loadFavoriteDishes()
            } else {
                loadDishesByType()
            }
        }
    }

    private fun loadFavoriteDishes() {
        dishesViewModel.getFavoriteDishes().observe(viewLifecycleOwner) { dishes ->
            updateUI(dishes)
        }
    }

    private fun loadDishesByType() {
        selectionViewModel.selectedDishType.observe(viewLifecycleOwner) { dishType ->
            if (dishType == null) return@observe

            dishesViewModel.getDishesForType(dishType.id)
                .observe(viewLifecycleOwner) { dishes ->
                    updateUI(dishes)
                }
        }
    }

    private fun updateUI(dishes: List<Dish>?) {
        if (dishes.isNullOrEmpty()) {
            Log.w("DishesFragment", "No dishes to display")
            Toast.makeText(requireContext(), R.string.no_dishes, Toast.LENGTH_SHORT).show()
            binding.recyclerDishes.adapter = null
            return
        }

        Log.d("DishesFragment", "Loaded ${dishes.size} dishes")

        val adapter = DishAdapter(dishes) { clickedDish ->
            try {
                Log.d("DishesFragment", "Dish clicked: ${clickedDish.name} (ID: ${clickedDish.id})")
                selectionViewModel.setDishId(clickedDish.id)
                findNavController().navigate(R.id.action_dishesFragment_to_dishDisplayPageFragment)

            } catch (e: Exception) {
                Log.e("DishesFragment", "Navigation error: ${e.message}", e)
                Toast.makeText(requireContext(), R.string.dish_nav_error, Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerDishes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDishes.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}