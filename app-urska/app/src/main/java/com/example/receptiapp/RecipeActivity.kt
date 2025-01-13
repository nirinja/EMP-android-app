package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject

class RecipeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recipeJson = intent.getStringExtra("RECIPE") ?: "{}"

        setContent {
            ReceptiAppTheme {
                RecipeScreen(recipeJson)
            }
        }
    }
}

@Composable
fun RecipeScreen(recipeJson: String) {
    val recipe = remember(recipeJson) {
        JSONObject(recipeJson).let {
            Recipe(
                name = it.getString("name"),
                ingredients = it.getJSONArray("ingredients").toList(), // Fixed to return List<String>
                instructions = it.getJSONArray("instructions").toList(), // Fixed to return List<String>
                prepTime = it.getInt("prepTimeMinutes"), // Fixed to use Int
                cookTime = it.getInt("cookTimeMinutes"), // Fixed to use Int
                difficulty = "Medium", // Optional: hardcoded or dynamic
                image = "https://via.placeholder.com/150" // Optional: hardcoded or dynamic
            )
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = recipe.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ingredients:",
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn {
            items(recipe.ingredients) { ingredient ->
                Text("- $ingredient", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Instructions:",
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn {
            items(recipe.instructions) { instruction ->
                Text("- $instruction", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Prep Time: ${recipe.prepTime} min | Cook Time: ${recipe.cookTime} min",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun JSONArray.toList(): List<String> {
    val list = mutableListOf<String>()
    for (i in 0 until length()) {
        list.add(getString(i))
    }
    return list
}

// Removed redundant Recipe declaration
