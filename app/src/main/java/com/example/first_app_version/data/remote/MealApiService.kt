package com.example.first_app_version.data.remote

import com.example.first_app_version.data.remote.model.CategoryResponse
import com.example.first_app_version.data.remote.model.MealDetailsResponse
import com.example.first_app_version.data.remote.model.MealsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService { //interface means a contract with no code

    @GET("categories.php")
    suspend fun getCategories(): Response<CategoryResponse>

    @GET("filter.php")
    suspend fun getMealsByCategory(
        @Query("c") category: String //query means adding parameters to the url
    ): Response<MealsResponse>

    @GET("lookup.php")
    suspend fun getMealDetails(
        @Query("i") mealId: String
    ): Response<MealDetailsResponse>

    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealDetailsResponse>
}
