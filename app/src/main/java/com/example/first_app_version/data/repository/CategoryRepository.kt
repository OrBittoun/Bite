import android.app.Application
import androidx.lifecycle.LiveData
import com.example.first_app_version.data.local_db.KitchenDataBase
import com.example.first_app_version.data.models.Category
import com.example.first_app_version.data.remote.RetrofitInstance
import com.example.first_app_version.data.remote.model.MealDetailsResponse
import com.example.first_app_version.data.remote.model.MealsResponse
import retrofit2.Response

class CategoryRepository(application: Application) {

    private val db = KitchenDataBase.getDataBase(application)
    private val dao = db.categoryDao()
    private val api = RetrofitInstance.api

    val categoriesFromDb: LiveData<List<Category>> =  dao.getAllCategories() //calling to api

    suspend fun fetchCategoriesFromApi() {

        try {

            val response = api.getCategories()

            if (response.isSuccessful) {

                response.body()?.let { categoryResponse ->

                    val categories = categoryResponse.categories.map { dto -> //convert dto to entity
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
            // כאן אנחנו תופסים UnknownHostException
            e.printStackTrace()
        }
    }

    suspend fun getMealsByCategory(category: String): Response<MealsResponse> {
        return api.getMealsByCategory(category)
    }

    suspend fun getMealDetails(mealId: String): Response<MealDetailsResponse> {
        return api.getMealDetails(mealId)
    }

}
