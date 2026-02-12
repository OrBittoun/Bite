package com.example.first_app_version.ui.all_kitchens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.data.repository.KitchenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KitchenViewModel @Inject constructor(
    private val repository: KitchenRepository
) : ViewModel() {

    val kitchens: LiveData<List<Kitchen>> = repository.getItems()

    private val _chosenKitchen = MutableLiveData<Kitchen>()
    val chosenKitchen: LiveData<Kitchen> get() = _chosenKitchen

    fun setKitchen(kitchen: Kitchen) {
        _chosenKitchen.value = kitchen
    }

    fun getKitchenSync(id: Int): Kitchen? = repository.getKitchen(id)
}