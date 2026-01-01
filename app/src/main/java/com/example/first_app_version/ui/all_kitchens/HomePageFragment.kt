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
import com.example.first_app_version.data.models.HomeCategory
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.databinding.HomePageLayoutBinding
import com.example.first_app_version.ui.KitchenViewModel
import com.example.first_app_version.ui.SelectionViewModel

class HomePageFragment : Fragment() {

    private var _binding: HomePageLayoutBinding? = null
    private val binding get() = _binding!!
    private val kitchenViewModel: KitchenViewModel by activityViewModels()
    private val selectionViewModel: SelectionViewModel by activityViewModels()

    private var kitchensCache: List<Kitchen> = emptyList()

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

        val categories = listOf(
            HomeCategory(
                kitchenName = "Italian",
                kitchenId = 1,
                previewDishIds = listOf(
                    1,  // Pizza - Neapolitan Pizza (dish_type_id = 1)
                    6,  // Pasta - Pasta Carbonara (dish_type_id = 2)
                    11  // Lasagna - Lasagna Bolognese (dish_type_id = 3)
                )
            ),
            HomeCategory(
                kitchenName = "Asian",
                kitchenId = 2,
                previewDishIds = listOf(
                    16, // Sushi - Salmon Nigiri (dish_type_id = 4)
                    21, // Dim Sum - Shrimp Dumplings (dish_type_id = 5)
                    26  // Ramen - Tonkotsu Ramen (dish_type_id = 6)
                )
            ),
            HomeCategory(
                kitchenName = "Meat & Fish",
                kitchenId = 4,
                previewDishIds = listOf(
                    46, // Grill - Beef Skewers (dish_type_id = 10)
                    50, // Burgers - Classic Burger (dish_type_id = 11)
                    54, // Stews - Beef Stew (dish_type_id = 12)
                    58, // Chicken - Grilled Chicken (dish_type_id = 13)
                    62  // Fish - Grilled Salmon (dish_type_id = 14)
                )
            ),
            HomeCategory(
                kitchenName = "Vegan",
                kitchenId = 3,
                previewDishIds = listOf(
                    31, // Vegan Bowls - Buddha Bowl (dish_type_id = 7)
                    36, // Vegan Salads - Fresh Green Salad (dish_type_id = 8)
                    41  // Vegan Mains - Lentil Stew (dish_type_id = 9)
                )
            ),
            HomeCategory(
                kitchenName = "Desserts",
                kitchenId = 5,
                previewDishIds = listOf(
                    66, // Cakes - Chocolate Cake (dish_type_id = 15)
                    70, // Ice Cream - Vanilla Ice Cream (dish_type_id = 16)
                    74  // Pastries - Croissant (dish_type_id = 17)
                )
            )
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
                    findNavController().navigate(
                        R.id.action_homePageFragment_to_dishDisplayPageFragment2
                    )
                } catch (e: Exception) {
                    Log.e("HomePageFragment", "Navigation error: ${e.message}")
                    Toast.makeText(
                        requireContext(),
                        "שגיאה בניווט למנה",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },

            onCategoryClick = { category ->
                try {
                    if (kitchensCache.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "הנתונים עדיין נטענים",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@HomeCategoriesAdapter
                    }

                    val selectedKitchen = kitchensCache.firstOrNull {
                        it.id == category.kitchenId
                    }

                    if (selectedKitchen != null) {
                        kitchenViewModel.setKitchen(selectedKitchen)
                        findNavController().navigate(
                            R.id.action_homePageFragment_to_dishesTypesFragment
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "לא נמצא מטבח",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Log.e("HomePageFragment", "Navigation error: ${e.message}")
                }
            }
        )

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter = adapter

        kitchenViewModel.kitchens?.observe(viewLifecycleOwner) { list ->
            kitchensCache = list ?: emptyList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPreviewImageForDish(dishId: Int): Int {
        return when (dishId) {
            // Italian
            1 -> R.drawable.napoli_pizza           // Pizza
            6 -> R.drawable.pasta_carbonara        // Pasta
            11 -> R.drawable.lasagna_bolognese     // Lasagna

            // Asian
            16 -> R.drawable.sushi_salmon_nigiri   // Sushi
            21 -> R.drawable.dim_sum_shrimp_dumplings // Dim Sum
            26 -> R.drawable.ramen_tonkotsu        // Ramen

            // Meat & Fish
            46 -> R.drawable.meat_beef_skewers     // Grill
            50 -> R.drawable.meat_classic_burger   // Burgers
            54 -> R.drawable.meat_beef_stew        // Stews
            58 -> R.drawable.grilled_chicken       // Chicken
            62 -> R.drawable.fish_grilled_salmon   // Fish

            // Vegan
            31 -> R.drawable.vegan_buddha_bowl     // Bowls
            36 -> R.drawable.vegan_green_salad     // Salads
            41 -> R.drawable.vegan_lentil_stew     // Mains

            // Desserts
            66 -> R.drawable.chocolate_cake        // Cakes
            70 -> R.drawable.vanilla_ice_cream     // Ice Cream
            74 -> R.drawable.croissant             // Pastries

            else -> R.drawable.default_dish
        }
    }
}