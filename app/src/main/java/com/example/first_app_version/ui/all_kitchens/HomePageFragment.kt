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

    // נחזיק את רשימת הבסיס כמשתנה מחלקה כדי לשמור על עקביות ה-IDs
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

        // הגדרת רשימת הבסיס -IDs קבועים מונעים בלבול בתמונות
        // בתוך onViewCreated ב-HomePageFragment.kt
        baseHomeCategories = listOf(
            HomeCategory(6, "Explore 🌍", listOf(10013, 10010, 10001, 10011, 10003)),
            HomeCategory(7, "My Favorites ❤️", listOf()),
            HomeCategory(1, kitchenViewModel.getKitchenSync(1)?.name ?: "Italian", listOf(1, 6, 11)),
            HomeCategory(2, kitchenViewModel.getKitchenSync(2)?.name ?: "Asian", listOf(16, 21, 26)),

            // כאן התיקון - החלפנו את ה-IDs כדי שיתאימו ל-Database
            HomeCategory(3, kitchenViewModel.getKitchenSync(3)?.name ?: "Vegan", listOf(31, 36, 41)),
            HomeCategory(4, kitchenViewModel.getKitchenSync(4)?.name ?: "Meat & Fish", listOf(46, 50, 54, 58, 62)),
            HomeCategory(5, kitchenViewModel.getKitchenSync(5)?.name ?: "Desserts", listOf(66, 70, 74))
        )
        val adapter = HomeCategoriesAdapter(
            categories = baseHomeCategories.filter { it.kitchenId != 7 }, // התחלה ללא מועדפים
            previewProvider = { category ->
                if (category.kitchenId == 6) {
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
                        DishPreview(dishId = dishId, imageRes = getPreviewImageForDish(dishId))
                    }
                }
            },
            onDishClick = { id ->
                if (id >= 10000) {
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
        prefetchLocalImages(baseHomeCategories, adapter)
    }

    private fun handleCategoryNavigation(category: HomeCategory) {
        Log.d("NavigationCheck", "Clicked on Kitchen ID: ${category.kitchenId} (${category.kitchenName})")

        try {
            if (category.kitchenId == 6) { // Explore
                selectionViewModel.setFavoritesMode(false)
                categoryViewModel.fetchCategories()
                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
                return
            }

            if (category.kitchenId == 7) { // Favorites
                selectionViewModel.setFavoritesMode(true)
                findNavController().navigate(R.id.action_homePageFragment_to_dishesFragment)
                return
            }

            // מטבח רגיל
            selectionViewModel.setFavoritesMode(false)

            // שליפת המטבח מה-ViewModel לפי ה-ID של הקטגוריה שנלחצה
            val selectedKitchen = kitchenViewModel.getKitchenSync(category.kitchenId)

            if (selectedKitchen != null) {
                Log.d("NavigationCheck", "Setting Kitchen in ViewModel: ${selectedKitchen.name}")
                kitchenViewModel.setKitchen(selectedKitchen) // כאן אנחנו מעדכנים את המטבח הנבחר
                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
            } else {
                // אם לא מצאנו ב-Sync, ננסה לחפש ב-Cache המקומי של הפרגמנט
                val fallbackKitchen = kitchensCache.find { it.id == category.kitchenId }
                if (fallbackKitchen != null) {
                    kitchenViewModel.setKitchen(fallbackKitchen)
                    findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
                } else {
                    Toast.makeText(requireContext(), "Kitchen not found (ID: ${category.kitchenId})", Toast.LENGTH_SHORT).show()
                }
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

            // יצירת רשימה מעודכנת המבוססת על רשימת הבסיס לשמירה על יציבות
            val updatedCategories = baseHomeCategories.map { category ->
                if (category.kitchenId == 7) {
                    category.copy(previewDishIds = favoriteIds)
                } else {
                    category
                }
            }.filter { it.kitchenId != 7 || it.previewDishIds.isNotEmpty() }

            // בדיקה אם הנתונים באמת השתנו לפני עדכון (מונע בלבול תמונות ורענון מיותר)
            if (currentAdapter.categories != updatedCategories) {
                prefetchLocalImages(updatedCategories, currentAdapter)
                currentAdapter.categories = updatedCategories
                currentAdapter.notifyDataSetChanged()
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