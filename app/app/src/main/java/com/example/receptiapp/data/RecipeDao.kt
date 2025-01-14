package com.example.receptiapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: Recipe): Long

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE recipeId = :id")
    suspend fun getRecipeById(id: Int): Recipe?

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipes WHERE name = :name LIMIT 1")
    suspend fun getRecipeByName(name: String): Recipe?

    @Query("DELETE FROM recipes WHERE name = :name")
    suspend fun deleteRecipeByName(name: String)

}