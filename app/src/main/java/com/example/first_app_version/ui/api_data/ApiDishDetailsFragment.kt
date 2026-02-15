package com.example.first_app_version.ui.dish_display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.first_app_version.R
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.remote.model.MealDetailsDto
import com.example.first_app_version.data.repository.DishRepository
import com.example.first_app_version.databinding.ApiDishDetailsBinding
import com.example.first_app_version.ui.api_data.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ApiDishDetailsFragment : Fragment() {

    private var _binding: ApiDishDetailsBinding? = null
    private val binding get() = _binding!!

    private val categoryViewModel: CategoryViewModel by activityViewModels()

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

        categoryViewModel.mealDetails.observe(viewLifecycleOwner) { mealDetails ->
            if (mealDetails != null) {
                updateUI(mealDetails)
            } else {
                Toast.makeText(requireContext(), "Failed to load meal details", Toast.LENGTH_SHORT).show()
            }
        }

        // תיקון שגיאה 1: שימוש ב-ID הנכון מה-XML
        binding.favoriteHeart.setOnClickListener {
            val mealDetails = categoryViewModel.mealDetails.value
            if (mealDetails != null) {
                saveDishToFavorites(mealDetails)
            }
        }
    }

    private fun updateUI(mealDetails: MealDetailsDto) {
        binding.dishTitle.text = mealDetails.strMeal
        binding.dishArea.text = "Origin: ${mealDetails.strArea ?: "Unknown"}"
        binding.dishCategory.text = "Category: ${mealDetails.strCategory ?: "Unknown"}"
        binding.dishInstructions.text = mealDetails.strInstructions ?: "No instructions available."

        val ingredientsText = getIngredientsText(mealDetails)
        binding.dishIngredients.text = ingredientsText

        // תיקון שגיאה 2: טעינה לתוך dish_img
        Glide.with(this)
            .load(mealDetails.strMealThumb)
            .placeholder(R.drawable.default_dish)
            .into(binding.dishImg)
    }

    private fun saveDishToFavorites(mealDetails: MealDetailsDto) {
        // תיקון שגיאה 3: הוספת restaurantName והגדרת כל הפרמטרים במדויק
        val newDish = Dish(
            id = mealDetails.idMeal.toIntOrNull() ?: System.currentTimeMillis().toInt(),
            dishTypeId = 0,
            name = mealDetails.strMeal,
            restaurantName = "Explore API", // הוספנו את שם המסעדה!
            imageRes = null,
            imageUrl = mealDetails.strMealThumb,
            isFavorite = true,
            reviewsCount = 0
        )

        lifecycleScope.launch {
            dishRepository.insertDish(newDish)
            Toast.makeText(requireContext(), "${mealDetails.strMeal} added to favorites!", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}