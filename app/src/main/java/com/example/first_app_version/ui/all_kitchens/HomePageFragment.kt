package com.example.first_app_version.ui.all_kitchens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.data.repository.DishRepository
import com.example.first_app_version.databinding.HomePageLayoutBinding
import com.example.first_app_version.ui.api_data.CategoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomePageFragment : Fragment() {

    private var _binding: HomePageLayoutBinding? = null
    private val binding get() = _binding!!

    private val kitchenViewModel: KitchenViewModel by activityViewModels()
    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()

    private lateinit var dishRepository: DishRepository
    private val dishImageCache = mutableMapOf<Int, Int>()
    private var kitchensCache: List<Kitchen> = emptyList()

    // רשימת בסיס קבועה שתמיד מכילה את כל הקטגוריות באותו סדר
    private lateinit var baseHomeCategories: List<HomeCategory>

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

        // 1. הגדרת רשימת הבסיס - המבנה תמיד קבוע (מקום 1 הוא תמיד מועדפים)
        baseHomeCategories = listOf(
            HomeCategory(6, "Explore \uD83C\uDF0D", listOf(10013, 10010, 10001, 10011, 10003)),
            HomeCategory(7, "My Favorites ❤️", listOf()),
            HomeCategory(1, kitchenViewModel.getKitchenSync(1)?.name ?: "Italian", listOf(1, 6, 11)),
            HomeCategory(2, kitchenViewModel.getKitchenSync(2)?.name ?: "Asian", listOf(16, 21, 26)),
            HomeCategory(3, kitchenViewModel.getKitchenSync(3)?.name ?: "Vegan", listOf(31, 36, 41)),
            HomeCategory(4, kitchenViewModel.getKitchenSync(4)?.name ?: "Meat & Fish", listOf(46, 50, 54, 58, 62)),
            HomeCategory(5, kitchenViewModel.getKitchenSync(5)?.name ?: "Desserts", listOf(66, 70, 74))
        )

        // 2. אתחול האדפטר עם הרשימה המלאה (כולל מועדפים ריקים בהתחלה)
        val adapter = HomeCategoriesAdapter(
            categories = baseHomeCategories,
            previewProvider = { category ->
                if (category.kitchenId == 6) {
                    category.previewDishIds.map { id ->
                        when (id) {
                            10013 -> DishPreview(id, "Breakfast", R.drawable.breakfast)
                            10010 -> DishPreview(id, "Starter", R.drawable.starter)
                            10001 -> DishPreview(id, "Beef", R.drawable.beef)
                            10011 -> DishPreview(id, "Vegan", R.drawable.vegan)
                            10003 -> DishPreview(id, "Dessert", R.drawable.desserts)
                            else -> DishPreview(id, "Unknown", R.drawable.default_dish)
                        }
                    }
                } else {
                    category.previewDishIds.map { dishId ->
                        DishPreview(dishId = dishId, imageRes = getPreviewImageForDish(dishId))
                    }
                }
            },
            onDishClick = { id ->
                if (id >= 10000) {
                    // לוגיקת אקספלור
                } else {
                    selectionViewModel.setDishId(id)
                    findNavController().navigate(R.id.action_homePageFragment_to_dishDisplayPageFragment2)
                }
            },
            onCategoryClick = { category -> handleCategoryNavigation(category) }
        )

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter = adapter

        setupObservers()
        prefetchLocalImages(baseHomeCategories, adapter)
    }

    private fun handleCategoryNavigation(category: HomeCategory) {
        try {
            if (category.kitchenId == 6) {
                selectionViewModel.setFavoritesMode(false)
                categoryViewModel.fetchCategories()
                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
                return
            }

            if (category.kitchenId == 7) {
                selectionViewModel.setFavoritesMode(true)
                findNavController().navigate(R.id.action_homePageFragment_to_dishesFragment)
                return
            }

            selectionViewModel.setFavoritesMode(false)
            val selectedKitchen = kitchenViewModel.getKitchenSync(category.kitchenId)
                ?: kitchensCache.find { it.id == category.kitchenId }

            if (selectedKitchen != null) {
                kitchenViewModel.setKitchen(selectedKitchen)
                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
            }
        } catch (e: Exception) {
            Log.e("HomePageFragment", "Navigation error: ${e.message}")
        }
    }

    private fun setupObservers() {
        kitchenViewModel.kitchens.observe(viewLifecycleOwner) { list ->
            kitchensCache = list ?: emptyList()
        }

        dishRepository.getFavoriteDishes().observe(viewLifecycleOwner) { favoriteDishes ->
            val favoriteIds = favoriteDishes.map { it.id }
            val currentAdapter = binding.homeRecyclerView.adapter as? HomeCategoriesAdapter ?: return@observe

            // יצירת רשימה מעודכנת - תמיד שומרים על המבנה המלא (בלי filter)
            val updatedCategories = baseHomeCategories.map { category ->
                if (category.kitchenId == 7) category.copy(previewDishIds = favoriteIds)
                else category
            }

            // עדכון האדפטר רק אם הנתונים במועדפים באמת השתנו
            if (currentAdapter.categories != updatedCategories) {
                currentAdapter.categories = updatedCategories
                // מעדכנים רק את שורת המועדפים (נמצאת תמיד באינדקס 1)
                currentAdapter.notifyItemChanged(1)

                // טעינת תמונות למנות החדשות שנוספו למועדפים
                prefetchLocalImages(updatedCategories, currentAdapter)
            }
        }
    }

    private fun prefetchLocalImages(categories: List<HomeCategory>, adapter: HomeCategoriesAdapter) {
        val allPreviewIds = categories.flatMap { it.previewDishIds }.distinct()
        lifecycleScope.launch {
            try {
                val resImgs = allPreviewIds.map { id ->
                    async(Dispatchers.IO) {
                        val res = dishRepository.getDishImageRes(id) ?: R.drawable.default_dish
                        id to res
                    }
                }
                resImgs.awaitAll().forEach { (id, res) ->
                    dishImageCache[id] = res
                }
                // לא מבצעים notifyDataSetChanged כללי כאן כדי למנוע קפיצות
            } catch (e: Exception) {
                Log.e("HomePageFragment", "Failed to prefetch", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPreviewImageForDish(dishId: Int): Int {
        return dishImageCache[dishId] ?: R.drawable.default_dish
    }
}