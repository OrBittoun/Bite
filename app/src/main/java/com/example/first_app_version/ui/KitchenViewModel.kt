package com.example.first_app_version.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.first_app_version.R
import com.example.first_app_version.data.models.HomeCategory
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.data.repository.KitchenRepository

class KitchenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KitchenRepository(application)

    val kitchens: LiveData<List<Kitchen>> =
        repository.getItems() ?: MutableLiveData(emptyList())

    val categories: MediatorLiveData<List<HomeCategory>> = MediatorLiveData<List<HomeCategory>>().apply {
        addSource(kitchens) { list ->
            value = list.map { k ->
                HomeCategory(
                    title = k.name,
                    previewImages = previewImagesForKitchen(k.name)
                )
            }
        }
    }

    private val _chosenKitchen = MutableLiveData<Kitchen>()
    val chosenKitchen: LiveData<Kitchen> get() = _chosenKitchen

    fun setKitchen(kitchen: Kitchen) {
        _chosenKitchen.value = kitchen
    }

    private fun previewImagesForKitchen(name: String): List<Int> = when (name) {
        "Italian" -> listOf(
            R.drawable.napoli_pizza,
            R.drawable.pasta_alfredo,
            R.drawable.lasagna_eggplant_parmesan
        )
        "Asian" -> listOf(
            R.drawable.sushi_california_roll,
            R.drawable.ramen_tonkotsu,
            R.drawable.dim_sum_beef_dumplings
        )
        "Meat & Fish" -> listOf(
            R.drawable.meat_goulash,
            R.drawable.meat_classic_burger,
            R.drawable.meat_entrecote,
            R.drawable.fish_teriyaki
        )
        "Vegan" -> listOf(
            R.drawable.vegan_chickpea_salad,
            R.drawable.vegan_stuffed_zucchini,
            R.drawable.vegan_buddha_bowl
        )
        "Desserts" -> listOf(
            R.drawable.carrot_cake,
            R.drawable.vanilla_ice_cream,
            R.drawable.cinnamon_roll
        )
        else -> emptyList()
    }
}