package com.example.first_app_version.ui.all_kitchens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import com.example.first_app_version.data.models.HomeCategory
import com.example.first_app_version.data.models.DishPreview
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.data.repository.DishRepository
import com.example.first_app_version.databinding.HomePageLayoutBinding
import com.example.first_app_version.ui.api_data.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private var _binding: HomePageLayoutBinding? = null
    private val binding get() = _binding!!

    private val kitchenViewModel: KitchenViewModel by activityViewModels()
    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()

    // הזרקת התלות של Hilt במקום ליצור ידנית!
    @Inject
    lateinit var dishRepository: DishRepository

    private val dishImageCache = mutableMapOf<Int, Int>()
    private var kitchensCache: List<Kitchen> = emptyList()

    private lateinit var adapter: HomeCategoriesAdapter
    private val homeCategories = mutableListOf<HomeCategory>()

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

        // יצירת הקטגוריות
        homeCategories.clear()
        homeCategories.addAll(
            listOf(
                HomeCategory(6, "Explore \uD83C\uDF0D", listOf(10099, 10013, 10010, 10001, 10011, 10003)),
                HomeCategory(7, "My Favorites ❤️", listOf()),
                HomeCategory(1, kitchenViewModel.getKitchenSync(1)?.name ?: "Italian", listOf(1, 6, 11)),
                HomeCategory(2, kitchenViewModel.getKitchenSync(2)?.name ?: "Asian", listOf(16, 21, 26)),
                HomeCategory(4, kitchenViewModel.getKitchenSync(4)?.name ?: "Meat & Fish", listOf(46, 50, 54, 58, 62)),
                HomeCategory(3, kitchenViewModel.getKitchenSync(3)?.name ?: "Vegan", listOf(31, 36, 41)),
                HomeCategory(5, kitchenViewModel.getKitchenSync(5)?.name ?: "Desserts", listOf(66, 70, 74))
            )
        )

        adapter = HomeCategoriesAdapter(
            categories = homeCategories,
            previewProvider = { category ->
                when (category.kitchenId) {
                    6 -> { // Explore
                        category.previewDishIds.map { id ->
                            when (id) {
                                10099 -> DishPreview(id, "Surprise Me!", R.drawable.surprise_me)
                                10013 -> DishPreview(dishId = id, categoryName = "Breakfast", imageRes = R.drawable.breakfast)
                                10010 -> DishPreview(dishId = id, categoryName = "Starter", imageRes = R.drawable.starter)
                                10001 -> DishPreview(dishId = id, categoryName = "Beef", imageRes = R.drawable.beef)
                                10011 -> DishPreview(dishId = id, categoryName = "Vegan", imageRes = R.drawable.vegan)
                                10003 -> DishPreview(dishId = id, categoryName = "Dessert", imageRes = R.drawable.desserts)
                                else -> DishPreview(dishId = id, categoryName = "Unknown", imageRes = R.drawable.default_dish)
                            }
                        }
                    }
                    7 -> { // Favorites - יטען דינמית
                        emptyList()
                    }
                    else -> { // קטגוריות רגילות
                        category.previewDishIds.map { dishId ->
                            DishPreview(
                                dishId = dishId,
                                imageRes = getPreviewImageForDish(dishId)
                            )
                        }
                    }
                }
            },
            onDishClick = { id ->
                if (id >= 10000) {
                    // בדיקת אינטרנט לפני גישה ל-API
                    if (!isNetworkAvailable()) {
                        val snackbar = com.google.android.material.snackbar.Snackbar.make(
                            binding.root,
                            R.string.no_internet_error,
                            com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                        )
                        val textView = snackbar.view.findViewById<android.widget.TextView>(
                            com.google.android.material.R.id.snackbar_text
                        )
                        textView.maxLines = 3
                        snackbar.show()
                    } else {
                        if (id == 10099) {
                            // Surprise Me - מנה רנדומלית
                            Log.d("HomePageFragment", "Loading random meal")
                            categoryViewModel.fetchRandomMeal()
                            findNavController().navigate(R.id.action_homePageFragment_to_apiDishDetailsFragment)
                        } else {
                            // קטגוריות אחרות
                            val selectedCategoryName = when (id) {
                                10013 -> "Breakfast"
                                10010 -> "Starter"
                                10001 -> "Beef"
                                10011 -> "Vegan"
                                10003 -> "Dessert"
                                else -> ""
                            }

                            if (selectedCategoryName.isNotEmpty()) {
                                kitchenViewModel.setKitchen(Kitchen(6, "Explore"))
                                categoryViewModel.getMealsByCategory(selectedCategoryName)
                                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
                            }
                        }
                    }
                } else {
                    // מנה מקומית
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
        loadFavorites()
        prefetchLocalImages(homeCategories, adapter)
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            dishRepository.getAllFavorites().observe(viewLifecycleOwner) { favoriteDishes ->

                val favoritesIndex = homeCategories.indexOfFirst { it.kitchenId == 7 }
                if (favoritesIndex != -1) {
                    val favoriteDishIds = favoriteDishes.map { it.id }
                    homeCategories[favoritesIndex] = homeCategories[favoritesIndex].copy(
                        previewDishIds = favoriteDishIds
                    )
                }

                // עדכן את ה-adapter עם המועדפים החדשים
                adapter.updateFavorites(favoriteDishes.map { dish ->
                    DishPreview(
                        dishId = dish.id,
                        categoryName = dish.name,
                        imageRes = dish.imageRes,
                        imageUrl = dish.imageUrl
                    )
                })

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun handleCategoryNavigation(category: HomeCategory) {
        try {
            if (category.kitchenId == 6) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(requireContext(), R.string.no_internet_error, Toast.LENGTH_SHORT).show()
                    return
                }
                kitchenViewModel.setKitchen(Kitchen(6, "Explore"))
                categoryViewModel.fetchCategories()
                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
                return
            }

            if (category.kitchenId == 7) {
                // התיקון שלנו: הפעלת מצב מועדפים ומעבר אמיתי למסך המנות!
                selectionViewModel.setFavoritesMode(true)
                findNavController().navigate(R.id.action_homePageFragment_to_dishesFragment)
                return
            }

            val selectedKitchen = kitchenViewModel.getKitchenSync(category.kitchenId)
                ?: kitchensCache.firstOrNull { it.id == category.kitchenId }

            if (selectedKitchen != null) {
                kitchenViewModel.setKitchen(selectedKitchen)
                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
            } else {
                Toast.makeText(requireContext(), R.string.no_kitchen_found, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("HomePageFragment", "Navigation error: ${e.message}", e)
        }
    }

    private fun setupObservers() {
        kitchenViewModel.kitchens.observe(viewLifecycleOwner) { list ->
            kitchensCache = list ?: emptyList()
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getPreviewImageForDish(dishId: Int): Int {
        return dishImageCache[dishId] ?: R.drawable.default_dish
    }
}