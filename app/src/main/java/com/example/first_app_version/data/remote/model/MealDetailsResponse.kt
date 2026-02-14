package com.example.first_app_version.data.remote.model

data class MealDetailsResponse(
    val meals: List<MealDetailsDto>
)

data class MealDetailsDto(
    val idMeal: String,
    val strMeal: String,
    val strInstructions: String,
    val strMealThumb: String
)
