package com.example.first_app_version.ui.dish_display

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.first_app_version.R
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.remote.model.MealDetailsDto
import com.example.first_app_version.data.repository.DishRepository
import com.example.first_app_version.databinding.ApiDishDetailsBinding
import com.example.first_app_version.ui.api_data.CategoryViewModel
import com.example.first_app_version.ui.all_kitchens.SelectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ApiDishDetailsFragment : Fragment() {

    private var _binding: ApiDishDetailsBinding? = null
    private val binding get() = _binding!!

    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val selectionViewModel: SelectionViewModel by activityViewModels()

    @Inject
    lateinit var dishRepository: DishRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ApiDishDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectionViewModel.selectedDishId.observe(viewLifecycleOwner) { dishId ->
            if (dishId == null) return@observe

            if (dishId == -1) {
                categoryViewModel.mealDetails.observe(viewLifecycleOwner) { mealDetails ->
                    if (mealDetails != null) {
                        updateUI(mealDetails)
                        setupFavoriteToggle(mealDetails)
                    }
                }
                return@observe
            }

            if (!isNetworkAvailable()) {
                dishRepository.getDishById(dishId).observe(viewLifecycleOwner) { localDish ->
                    if (localDish != null) {
                        showOfflineData(localDish)
                    } else {
                        Toast.makeText(requireContext(), "No internet and dish not saved.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                categoryViewModel.fetchMealDetails(dishId.toString())
                categoryViewModel.mealDetails.observe(viewLifecycleOwner) { mealDetails ->
                    if (mealDetails != null && mealDetails.idMeal == dishId.toString()) {
                        updateUI(mealDetails)
                        setupFavoriteToggle(mealDetails)
                    }
                }
            }
        }
    }

    private fun showOfflineData(dish: Dish) {
        binding.dishTitle.text = dish.name
        binding.dishArea.text = "Origin: Saved Offline"
        binding.dishCategory.text = "Category: Explore API"

        val recipe = dish.description ?: ""
        if (recipe.contains("\n\nInstructions:\n")) {
            val parts = recipe.split("\n\nInstructions:\n")
            binding.dishIngredients.text = parts[0].replace("Ingredients:\n", "")
            binding.dishInstructions.text = parts[1]
        } else {
            binding.dishIngredients.text = "See instructions below"
            binding.dishInstructions.text = recipe.ifEmpty { "No recipe saved." }
        }

        Glide.with(this)
            .load(dish.imageUrl)
            .placeholder(R.drawable.default_dish)
            .into(binding.dishImg)

        binding.favoriteHeart.setImageResource(if (dish.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border)

        binding.favoriteHeart.setOnClickListener {
            if (dish.isFavorite) {
                lifecycleScope.launch {
                    dishRepository.deleteDish(dish)
                    Toast.makeText(requireContext(), "${dish.name} removed from favorites", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun setupFavoriteToggle(mealDetails: MealDetailsDto) {
        val mealId = mealDetails.idMeal.toIntOrNull() ?: return

        dishRepository.getDishById(mealId).observe(viewLifecycleOwner) { localDish ->
            val isFavorite = localDish != null && localDish.isFavorite
            val heartIcon = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            binding.favoriteHeart.setImageResource(heartIcon)

            binding.favoriteHeart.setOnClickListener {
                if (isFavorite) {
                    lifecycleScope.launch {
                        dishRepository.deleteDish(localDish!!)
                        Toast.makeText(requireContext(), "${mealDetails.strMeal} removed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    saveDishToFavorites(mealDetails)
                }
            }
        }
    }

    private fun updateUI(mealDetails: MealDetailsDto) {
        binding.dishTitle.text = mealDetails.strMeal
        binding.dishArea.text = "Origin: ${mealDetails.strArea ?: "Unknown"}"
        binding.dishCategory.text = "Category: ${mealDetails.strCategory ?: "Unknown"}"
        binding.dishInstructions.text = mealDetails.strInstructions ?: "No instructions available."
        binding.dishIngredients.text = getIngredientsText(mealDetails)

        Glide.with(this)
            .load(mealDetails.strMealThumb)
            .placeholder(R.drawable.default_dish)
            .into(binding.dishImg)
    }

    private fun saveDishToFavorites(mealDetails: MealDetailsDto) {
        val fullRecipe = "Ingredients:\n${getIngredientsText(mealDetails)}\n\nInstructions:\n${mealDetails.strInstructions}"
        val newDish = Dish(
            id = mealDetails.idMeal.toIntOrNull() ?: System.currentTimeMillis().toInt(),
            name = mealDetails.strMeal,
            dishTypeId = 0,
            restaurantName = "Explore API",
            imageRes = null,
            imageUrl = mealDetails.strMealThumb,
            description = fullRecipe,
            isFavorite = true,
            price = 0,
            reviewsCount = 0
        )

        lifecycleScope.launch {
            dishRepository.insertDish(newDish)
            Toast.makeText(requireContext(), "${mealDetails.strMeal} saved!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getIngredientsText(meal: MealDetailsDto): String {
        val ingredients = java.lang.StringBuilder()
        val mealIngredients = listOf(
            meal.strIngredient1 to meal.strMeasure1, meal.strIngredient2 to meal.strMeasure2,
            meal.strIngredient3 to meal.strMeasure3, meal.strIngredient4 to meal.strMeasure4,
            meal.strIngredient5 to meal.strMeasure5, meal.strIngredient6 to meal.strMeasure6,
            meal.strIngredient7 to meal.strMeasure7, meal.strIngredient8 to meal.strMeasure8,
            meal.strIngredient9 to meal.strMeasure9, meal.strIngredient10 to meal.strMeasure10,
            meal.strIngredient11 to meal.strMeasure11, meal.strIngredient12 to meal.strMeasure12,
            meal.strIngredient13 to meal.strMeasure13, meal.strIngredient14 to meal.strMeasure14,
            meal.strIngredient15 to meal.strMeasure15, meal.strIngredient16 to meal.strMeasure16,
            meal.strIngredient17 to meal.strMeasure17, meal.strIngredient18 to meal.strMeasure18,
            meal.strIngredient19 to meal.strMeasure19, meal.strIngredient20 to meal.strMeasure20
        )

        for (item in mealIngredients) {
            val ingredient = item.first
            val measure = item.second
            if (!ingredient.isNullOrBlank()) {
                ingredients.append("• $ingredient")
                if (!measure.isNullOrBlank()) {
                    ingredients.append(" ($measure)")
                }
                ingredients.append("\n")
            }
        }
        return if (ingredients.isEmpty()) "No ingredients found." else ingredients.toString()
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
}