package com.example.receptiapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun categoryDao(): CategoryDao
}