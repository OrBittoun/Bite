package com.example.first_app_version.ui.all_dishes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.data.repository.DishRepository



class DishesViewModel(application: Application) : AndroidViewModel(application) {

    // שימי לב לשם המשתנה כאן: repository
    private val repository = DishRepository(application)

    fun getDishesForType(dishTypeId: Int): LiveData<List<Dish>> =
        repository.getDishesForDishType(dishTypeId)

    // תיקון: משתמשים ב-repository כפי שהגדרנו למעלה
    fun getFavoriteDishes(): LiveData<List<Dish>> {
        return repository.getFavoriteDishes()
    }
}