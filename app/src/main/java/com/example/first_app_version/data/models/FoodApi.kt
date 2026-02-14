package com.example.first_app_version.data.models
import retrofit2.http.GET

interface FoodApi {
    // נניח שיש לנו נתיב כזה ב-API
    @GET("random_food_quote")
    suspend fun getRandomQuote(): QuoteResponse
}