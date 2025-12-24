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
    indices = [Index(value = ["dish_id"])]
)
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Link to the dish this comment is about
    @ColumnInfo(name = "dish_id")
    val dishId: Int,

    // For now, local-only: default author name is "You"
    @ColumnInfo(name = "author_name")
    val authorName: String = "You",

    // Rating 1–5 (if you want ratings; can be optional if not needed yet)
    @ColumnInfo(name = "rating")
    val rating: Int,

    // The comment text content
    @ColumnInfo(name = "text")
    val text: String,

    // Human-readable timestamp string, e.g., "14-10-2025, 14:37"
    @ColumnInfo(name = "created_at")
    val createdAt: String,

    // Upvotes counter
    @ColumnInfo(name = "upvotes")
    val upvotes: Int = 0
)