package com.example.first_app_version.data.models

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dishes",
    indices = [Index(value = ["dish_type_id"])]
)
data class Dish(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "dish_type_id")
    val dishTypeId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "restaurantName")
    val restaurantName: String,

    @ColumnInfo(name = "image_res")
    @param:DrawableRes val imageRes: Int? = null,

    @ColumnInfo(name = "image_url") val imageUrl: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "price", defaultValue = "0")
    val price: Int = 0,

    @ColumnInfo(name = "reviews_count", defaultValue = "0")
    val reviewsCount: Int = 0,

    @ColumnInfo(name = "is_favorite", defaultValue = "0")
    val isFavorite: Boolean = false
)