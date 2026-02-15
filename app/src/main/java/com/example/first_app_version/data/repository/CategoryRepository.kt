package com.example.first_app_version.data.repository

import androidx.lifecycle.LiveData
import com.example.first_app_version.data.local_db.CategoryDao
import com.example.first_app_version.data.models.Category
import com.example.first_app_version.data.remote.MealApiService
import com.example.first_app_version.data.remote.model.MealDetailsResponse
import com.example.first_app_version.data.remote.model.MealsResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val dao: CategoryDao,
    private val api: MealApiService
) {

    val categoriesFromDb: LiveData<List<Category>> = dao.getAllCategories()

    suspend fun fetchCategoriesFromApi() {
        try {
            val response = api.getCategories()
            if (response.isSuccessful) {
                response.body()?.let { categoryResponse ->
                    val categories = categoryResponse.categories.map { dto ->
                        Category(
                            id = dto.idCategory,
                            name = dto.strCategory,
                            thumbnail = dto.strCategoryThumb,
                            description = dto.strCategoryDescription
                        )
                    }
                    dao.clearAll()
                    dao.insertAll(categories)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getMealsByCategory(category: String): Response<MealsResponse> {
        return api.getMealsByCategory(category)
    }

    suspend fun getMealDetails(mealId: String): Response<MealDetailsResponse> {
        return api.getMealDetails(mealId)
    }

    suspend fun getRandomMeal(): Response<MealDetailsResponse> {
        return api.getRandomMeal()
    }
}