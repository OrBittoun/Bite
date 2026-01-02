package com.example.first_app_version.ui.all_kitchens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.ui.all_kitchens.HomeCategory
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.data.repository.DishRepository
import com.example.first_app_version.databinding.HomePageLayoutBinding

class HomePageFragment : Fragment() {

    private var _binding: HomePageLayoutBinding? = null
    private val binding get() = _binding!!
    private val kitchenViewModel: KitchenViewModel by activityViewModels()
    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private var kitchensCache: List<Kitchen> = emptyList()

    private lateinit var dishRepository: DishRepository
    private val dishImageCache = mutableMapOf<Int, Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomePageLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dishRepository = DishRepository(requireActivity().application)

        val categories = listOf(
            HomeCategory(1, kitchenViewModel.getKitchenSync(1)?.name ?: "Fail1", listOf(1, 6, 11)),
            HomeCategory(2, kitchenViewModel.getKitchenSync(2)?.name ?: "Asian", listOf(16, 21, 26)),
            HomeCategory(3, kitchenViewModel.getKitchenSync(3)?.name ?: "Meat & Fish", listOf(46, 50, 54, 58, 62)),
            HomeCategory(4, kitchenViewModel.getKitchenSync(4)?.name ?: "Vegan", listOf(31, 36, 41)),
            HomeCategory(5, kitchenViewModel.getKitchenSync(5)?.name ?: "Desserts", listOf(66, 70, 74))
        )

        val adapter = HomeCategoriesAdapter(
            categories = categories,
            previewProvider = { category ->
                category.previewDishIds.map { dishId ->
                    DishPreview(
                        dishId = dishId,
                        imageRes = getPreviewImageForDish(dishId)
                    )
                }
            },

            onDishClick = { dishId ->
                try {
                    Log.d("HomePageFragment", "Dish clicked: $dishId")
                    selectionViewModel.setDishId(dishId)
                    findNavController().navigate(R.id.action_homePageFragment_to_dishDisplayPageFragment2)
                } catch (e: Exception) {
                    Log.e("HomePageFragment", "Navigation error: ${e.message}")
                    Toast.makeText(requireContext(), R.string.dish_nav_error, Toast.LENGTH_SHORT).show()
                }
            },

            onCategoryClick = { category ->
                try {
                    val selectedKitchen = kitchenViewModel.getKitchenSync(category.kitchenId)
                        ?: kitchensCache.firstOrNull { it.id == category.kitchenId }

                    if (selectedKitchen != null) {
                        kitchenViewModel.setKitchen(selectedKitchen)
                        findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
                    } else {
                        Toast.makeText(requireContext(), R.string.no_kitchen_found, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("HomePageFragment", "Navigation error: ${e.message}")
                }
            }
        )

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter = adapter

        kitchenViewModel.kitchens.observe(viewLifecycleOwner) { list ->
            kitchensCache = list ?: emptyList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPreviewImageForDish(dishId: Int): Int {
        // If already fetched it, return cached value
        dishImageCache[dishId]?.let { return it }

        val res = dishRepository.getDishImageRes(dishId) ?: R.drawable.default_dish
        dishImageCache[dishId] = res
        return res
    }
}