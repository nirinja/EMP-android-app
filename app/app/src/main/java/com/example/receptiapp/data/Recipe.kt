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
    val ingredients: String,
    val instructions: String,
    val prepTime: Int,
    val cookTime: Int,
    val difficulty: String,
    val image: String,
    val mealType: String,
    val categoryId: Int?
)