package com.example.first_app_version.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = Dish::class,
            parentColumns = ["id"],
            childColumns = ["dish_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["dish_id"]),
        // Enforce "one comment per author per dish"
        Index(value = ["dish_id", "author_name"], unique = true)
    ]
)
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "dish_id")
    val dishId: Int,

    @ColumnInfo(name = "author_name")
    val authorName: String = "You",

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "text")
    val text: String,

    // Human-readable timestamp string, e.g., "14-10-2025, 14:37"
    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "upvotes")
    val upvotes: Int = 0
)