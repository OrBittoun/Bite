package com.example.first_app_version.ui.all_dishes_types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.data.models.DishTypeForRetro
import com.example.first_app_version.databinding.DishesTypesLayoutBinding
import com.example.first_app_version.ui.all_kitchens.KitchenViewModel
import com.example.first_app_version.ui.all_kitchens.SelectionViewModel
import com.example.first_app_version.ui.api_data.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DishesTypesFragment : Fragment() {

    private var _binding: DishesTypesLayoutBinding? = null
    private val binding get() = _binding!!

    private val kitchenViewModel: KitchenViewModel by activityViewModels()
    private val dishesTypesViewModel: DishesTypesViewModel by viewModels()
    private val selectionViewModel: SelectionViewModel by activityViewModels()

    private val categoryViewModel: CategoryViewModel by activityViewModels()

    private lateinit var adapter: DishTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DishesTypesLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DishTypeAdapter { dishType ->
            if (dishType.kitchenId == 6) {
                selectionViewModel.setDishId(dishType.id)
                findNavController().navigate(R.id.action_dishesTypesFragment_to_apiDishDetailsFragment)
            } else {
                val localDishType = DishType(
                    id = dishType.id,
                    kitchenId = dishType.kitchenId,
                    name = dishType.name,
                    imageRes = dishType.imageRes as? Int
                )
                selectionViewModel.setDishType(localDishType)
                selectionViewModel.setFavoritesMode(false)
                findNavController().navigate(R.id.action_dishesTypesFragment_to_dishesFragment)
            }
        }

        binding.recyclerDishesTypes.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerDishesTypes.adapter = adapter

        kitchenViewModel.chosenKitchen.observe(viewLifecycleOwner) { kitchen ->
            if (kitchen == null) {
                Toast.makeText(requireContext(), R.string.no_kitchen_found, Toast.LENGTH_SHORT).show()
                adapter.submitList(emptyList())
                return@observe
            }

            if (kitchen.id == 6) {
                categoryViewModel.meals.observe(viewLifecycleOwner) { mealList ->
                    if (!mealList.isNullOrEmpty()) {
                        val uiList = mealList.take(8).map { meal ->
                            DishTypeForRetro(
                                id = meal.idMeal.toInt(),
                                name = meal.strMeal,
                                kitchenId = 6,
                                imageRes = meal.strMealThumb
                            )
                        }
                        adapter.submitList(uiList)
                    } else {
                        Toast.makeText(requireContext(), R.string.api_no_categories_found, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                dishesTypesViewModel.getDishTypesForKitchen(kitchen.id).observe(viewLifecycleOwner) { dishTypes ->
                    if (dishTypes.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.no_dish_types_found, Toast.LENGTH_SHORT).show()
                    }
                    val uiList = dishTypes.map { dbType ->
                        DishTypeForRetro(
                            id = dbType.id,
                            name = dbType.name,
                            kitchenId = dbType.kitchenId,
                            imageRes = dbType.imageRes ?: R.drawable.default_dish
                        )
                    }
                    adapter.submitList(uiList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}