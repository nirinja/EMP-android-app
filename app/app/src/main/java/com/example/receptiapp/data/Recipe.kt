package com.example.receptiapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "recipes",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.SET_NULL // če kategorijo izbrišemo, se vrednost nastavi na null
    )]
)
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Int = 0,
    val name: String,
    val description: String,
    val instructions: String,
    val imageUrl: String?,
    val categoryId: Int? // Lahko je null, če recept še nima določene kategorije
)