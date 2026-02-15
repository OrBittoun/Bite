package com.example.first_app_version.ui.all_kitchens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.first_app_version.data.models.HomeCategory
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.data.repository.DishRepository
import com.example.first_app_version.data.repository.KitchenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KitchenViewModel @Inject constructor(
    private val kitchenRepository: KitchenRepository,
    private val dishRepository: DishRepository
) : ViewModel() {

    val kitchens: LiveData<List<Kitchen>> = kitchenRepository.getItems()

    private val _chosenKitchen = MutableLiveData<Kitchen>()
    val chosenKitchen: LiveData<Kitchen> get() = _chosenKitchen

    // --- הפונקציה שחסרה לשותף עבור מסך הבית ---
    fun getKitchenSync(id: Int): Kitchen? {
        return kitchens.value?.find { it.id == id }
    }

    private val _homeCategories = MutableLiveData<List<HomeCategory>>()
    val homeCategories: LiveData<List<HomeCategory>> = _homeCategories

    private val _dishImagesMap = MutableLiveData<Map<Int, Int>>()
    val dishImagesMap: LiveData<Map<Int, Int>> = _dishImagesMap

    init {
        loadHomeData()
    }

    fun setKitchen(kitchen: Kitchen) {
        _chosenKitchen.value = kitchen
    }

    private fun loadHomeData() {
        viewModelScope.launch(Dispatchers.IO) {
            val rawCategories = listOf(
                Triple(1, "Italian", listOf(1, 6, 11)),
                Triple(2, "Asian", listOf(16, 21, 26)),
                Triple(3, "Vegan", listOf(31, 36, 41)),
                Triple(4, "Meat & Fish", listOf(46, 50, 54, 58, 62)),
                Triple(5, "Desserts", listOf(66, 70, 74))
            )

            val categoriesList = rawCategories.map { (id, defaultName, dishIds) ->
                val kitchen = try {
                    kitchenRepository.getKitchen(id)
                } catch (e: Exception) { null }

                HomeCategory(id, kitchen?.name ?: defaultName, dishIds)
            }
            _homeCategories.postValue(categoriesList)

            val allDishIds = categoriesList.flatMap { it.previewDishIds }.distinct()
            val imagesDeferred = allDishIds.map { id ->
                async {
                    val res = dishRepository.getDishImageRes(id) ?: com.example.first_app_version.R.drawable.default_dish
                    id to res
                }
            }
            val imagesMap = imagesDeferred.awaitAll().toMap()
            _dishImagesMap.postValue(imagesMap)
        }
    }
}