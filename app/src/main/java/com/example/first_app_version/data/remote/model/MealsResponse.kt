package com.example.first_app_version.data.remote.model

data class MealsResponse(
    val meals: List<MealDto>
)

data class MealDto(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)
