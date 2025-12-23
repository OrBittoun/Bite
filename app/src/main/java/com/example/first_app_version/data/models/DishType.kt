package com.example.first_app_version.data.models

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "dish_types",
    foreignKeys = [
        ForeignKey(
            entity = Kitchen::class,
            parentColumns = ["id"],
            childColumns = ["kitchen_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["kitchen_id"])])
data class DishType(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Represents the kitchen this type belongs to
    @ColumnInfo(name = "kitchen_id")
    val kitchenId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "image_res")
    @DrawableRes val imageRes: Int? = null,

    @ColumnInfo(name = "description")
    val description: String? = null
)



