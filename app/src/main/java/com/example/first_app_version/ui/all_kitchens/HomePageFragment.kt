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
import com.example.first_app_version.databinding.HomePageLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private var _binding: HomePageLayoutBinding? = null
    private val binding get() = _binding!!

    private val kitchenViewModel: KitchenViewModel by activityViewModels()
    private val selectionViewModel: SelectionViewModel by activityViewModels()

    private var currentImagesMap: Map<Int, Int> = emptyMap()

    private lateinit var adapter: HomeCategoriesAdapter

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

        adapter = HomeCategoriesAdapter(
            previewProvider = { category ->
                category.previewDishIds.map { dishId ->
                    DishPreview(
                        dishId = dishId,
                        imageRes = currentImagesMap[dishId] ?: R.drawable.default_dish
                    )
                }
            },
            onDishClick = { dishId ->
                try {
                    selectionViewModel.setDishId(dishId)
                    findNavController().navigate(R.id.action_homePageFragment_to_dishDisplayPageFragment2)
                } catch (e: Exception) {
                    Log.e("HomePageFragment", "Navigation error", e)
                }
            },
            onCategoryClick = { category ->
                val kitchenList = kitchenViewModel.kitchens.value ?: emptyList()
                val selectedKitchen = kitchenList.find { it.id == category.kitchenId }

                if (selectedKitchen != null) {
                    kitchenViewModel.setKitchen(selectedKitchen)
                    findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
                } else {
                    Toast.makeText(requireContext(), R.string.no_kitchen_found, Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter = adapter

        kitchenViewModel.homeCategories.observe(viewLifecycleOwner) { categories ->
            adapter.updateCategories(categories)
        }

        kitchenViewModel.dishImagesMap.observe(viewLifecycleOwner) { images ->
            currentImagesMap = images
            adapter.notifyDataSetChanged()
        }

        // מאזינים לרשימת המטבחים רק כדי ש-Room ישלוף אותם מהדאטבייס (Lazy Loading)
        kitchenViewModel.kitchens.observe(viewLifecycleOwner) { kitchens ->
            // אין צורך לכתוב כאן כלום.
            // עצם ההאזנה גורמת ל-LiveData להתמלא בנתונים, והלחיצה שלך תעבוד!
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}