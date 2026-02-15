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

        val homeCategories = listOf(
            // הגדרת מטבח 6 עם ה-IDs המיוחדים למניעת התנגשות עם Room
            HomeCategory(6, "Explore \uD83C\uDF0D", listOf(10013, 10010, 10001, 10011, 10003)),
            HomeCategory(7, "My Favorites ❤\uFE0F", listOf()),
            HomeCategory(1, kitchenViewModel.getKitchenSync(1)?.name ?: "Italian", listOf(1, 6, 11)),
            HomeCategory(2, kitchenViewModel.getKitchenSync(2)?.name ?: "Asian", listOf(16, 21, 26)),
            HomeCategory(3, kitchenViewModel.getKitchenSync(3)?.name ?: "Meat & Fish", listOf(46, 50, 54, 58, 62)),
            HomeCategory(4, kitchenViewModel.getKitchenSync(4)?.name ?: "Vegan", listOf(31, 36, 41)),
            HomeCategory(5, kitchenViewModel.getKitchenSync(5)?.name ?: "Desserts", listOf(66, 70, 74))
        )

        val adapter = HomeCategoriesAdapter(
            categories = homeCategories,
            previewProvider = { category ->
                if (category.kitchenId == 6) { //explore
                    category.previewDishIds.map { id ->
                        when (id) {
                            10013 -> DishPreview(dishId = id, categoryName = "Breakfast", imageRes = R.drawable.breakfast)
                            10010 -> DishPreview(dishId = id, categoryName = "Starter", imageRes = R.drawable.starter)
                            10001 -> DishPreview(dishId = id, categoryName = "Beef", imageRes = R.drawable.beef)
                            10011 -> DishPreview(dishId = id, categoryName = "Vegan", imageRes = R.drawable.vegan)
                            10003 -> DishPreview(dishId = id, categoryName = "Dessert", imageRes = R.drawable.desserts)
                            else -> DishPreview(dishId = id, categoryName = "Unknown", imageRes = R.drawable.default_dish)
                        }
                    }
                } else {
                    category.previewDishIds.map { dishId ->
                        DishPreview(
                            dishId = dishId,
                            imageRes = getPreviewImageForDish(dishId)
                        )
                    }
                }
            },
            onDishClick = { id ->
                // שלב 3: מניעת התנגשות בניווט
                if (id >= 10000) {
                    // כאן נטפל בעתיד בפתיחת מסך 8 המנות לפי שם הקטגוריה
                    val selectedCategoryName = when(id) {
                        10013 -> "Breakfast"
                        10010 -> "Starter"
                        10001 -> "Beef"
                        10011 -> "Vegan"
                        10003 -> "Dessert"
                        else -> ""
                    }
                    Toast.makeText(requireContext(), "Opening $selectedCategoryName...", Toast.LENGTH_SHORT).show()
                } else {
                    // ניווט רגיל למנה מקומית
                    selectionViewModel.setDishId(id)
                    findNavController().navigate(R.id.action_homePageFragment_to_dishDisplayPageFragment2)
                }
            },
            onCategoryClick = { category ->
                handleCategoryNavigation(category)
            }
        )

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter = adapter

        setupObservers()
        prefetchLocalImages(homeCategories, adapter)
    }

    // שאר הפונקציות (handleCategoryNavigation, prefetchLocalImages וכו') נשארות ללא שינוי...
    private fun handleCategoryNavigation(category: HomeCategory) {
        try {
            if (category.kitchenId == 6) {
                categoryViewModel.fetchCategories()
                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
                return
            }

            if (category.kitchenId == 7) { // מועדפים
                selectionViewModel.setFavoritesMode(true)
                // ניווט ישירות למסך המנות
                findNavController().navigate(R.id.action_homePageFragment_to_dishesFragment)
                return
            }

            // לוגיקה רגילה לשאר הקטגוריות
            selectionViewModel.setFavoritesMode(false)

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

    private fun setupObservers() {
        kitchenViewModel.kitchens.observe(viewLifecycleOwner) { list ->
            kitchensCache = list ?: emptyList()
        }

        dishRepository.getFavoriteDishes().observe(viewLifecycleOwner) { favoriteDishes ->
            val favoriteIds = favoriteDishes.map { it.id }

            val currentAdapter = binding.homeRecyclerView.adapter as? HomeCategoriesAdapter
            currentAdapter?.let { adapter ->

                // 1. יצירת רשימת קטגוריות מעודכנת
                val updatedCategories = adapter.categories.map { category ->
                    if (category.kitchenId == 7) {
                        category.copy(previewDishIds = favoriteIds)
                    } else {
                        category
                    }
                }

                // 2. סינון: הצג את קטגוריה 7 רק אם יש בה מנות
                // שאר הקטגוריות (1-6) תמיד יוצגו
                val filteredCategories = updatedCategories.filter { category ->
                    category.kitchenId != 7 || category.previewDishIds.isNotEmpty()
                }

                // 3. טעינת תמונות ועדכון האדפטר
                prefetchLocalImages(filteredCategories, adapter)
                adapter.categories = filteredCategories
                adapter.notifyDataSetChanged()
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
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("HomePageFragment", "Failed to prefetch dish images", e)
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