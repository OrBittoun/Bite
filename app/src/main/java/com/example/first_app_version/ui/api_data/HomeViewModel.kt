import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CategoryRepository(application)

    val categories = repository.categoriesFromDb

    fun loadCategories() {
        viewModelScope.launch {
            repository.fetchCategoriesFromApi()
        }
    }
}
