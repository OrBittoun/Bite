package com.example.first_app_version.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.data.repository.KitchenRepository

class KitchenViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = KitchenRepository(application)

    val kitchens : LiveData<List<Kitchen>>? = repository.getItems()

    private val _chosenKitchen = MutableLiveData<Kitchen>()
    val chosenKitchen : LiveData<Kitchen> get () = _chosenKitchen

    fun setKitchen(kitchen: Kitchen) {
        _chosenKitchen.value = kitchen
    }

}
