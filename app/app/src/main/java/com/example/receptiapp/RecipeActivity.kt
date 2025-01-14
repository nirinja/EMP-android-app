package com.example.receptiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.json.JSONObject

class RecipeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fetch recipe data from the Intent
        val recipeData = intent.getStringExtra("RECIPE") ?: ""

        setContent {
            ReceptiAppTheme {
                if (recipeData.isNotEmpty()) {
                    RecipeScreen(recipeJson = recipeData, onBackClick = { finish() })
                } else {
                    ErrorScreen()
                }
            }
        }
    }

}

public fun saveRecipe(recipeData: String) {
    val recipe = JSONObject(recipeData).let { json ->
        Recipe(
            name = json.getString("name"),
            ingredients = json.getJSONArray("ingredients").toList(),
            instructions = json.getJSONArray("instructions").toList(),
            prepTime = json.getInt("prepTimeMinutes"),
            cookTime = json.getInt("cookTimeMinutes"),
            difficulty = json.getString("difficulty"),
            image = json.getString("image"),
            mealType = json.getJSONArray("mealType").toList()
        )
    }
    //recipeViewModel.saveRecipe(recipe)
}

@Composable
fun RecipeScreen(recipeJson: String, onBackClick: () -> Unit) {
    val recipe = remember { JSONObject(recipeJson) }
    val name = recipe.getString("name")
    val ingredients = recipe.getJSONArray("ingredients")
    val instructions = recipe.getJSONArray("instructions")
    val prepTime = recipe.getInt("prepTimeMinutes")
    val cookTime = recipe.getInt("cookTimeMinutes")
    val difficulty = recipe.getString("difficulty")
    val imageUrl = recipe.getString("image")
    val mealType = recipe.getJSONArray("mealType").join("\n")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(vertical = 8.dp)
        ) {
            // Image with gradient overlay
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Recipe Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )

                // Gradient overlay (transparent to black from top to middle)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Black, Color.Transparent),
                                startY = 0f,
                                endY = 300f
                            )
                        )
                )
            }

            // IconButton (instead of "Back" text, using an icon)
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                // Replace with your custom icon
                Icon(
                    painter = painterResource(id = R.drawable.left_big),  // Replace ic_back with your custom icon
                    contentDescription = "Back",
                    tint = Color.White // Set the tint color, or you can leave it as is
                )
            }

            IconButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.save),
                    contentDescription = "Saved Recipes",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Meal types displayed in a Row at the bottom left
            if (recipe.getJSONArray("mealType").length() > 0) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 10.dp, bottom = 10.dp)
                ) {
                    recipe.getJSONArray("mealType").let { mealTypes ->
                        for (i in 0 until mealTypes.length()) {
                            val meal = mealTypes.getString(i)
                            Box(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.6f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = meal,
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
        )

        Text(
            text = "Preparation: $prepTime min | Difficulty: $difficulty",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        // Ingredients listed in a Column with smaller spacing
        Column(modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)) {
            for (i in 0 until ingredients.length()) {
                val ingredient = ingredients.getString(i)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 2.dp) // Reduced bottom padding
                ) {
                    // Bullet point (using a character for bullet)
                    Text(
                        text = "•",
                        style = TextStyle(
                            fontSize = 20.sp, // Bigger bullet size
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Display the ingredient text
                    Text(
                        text = ingredient,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }

        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Column(modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)) {
            for (i in 0 until instructions.length()) {
                val instruction = instructions.getString(i)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 2.dp) // Reduced bottom padding
                ) {
                    // Bullet point (using a character for bullet)
                    Text(
                        text = "•",
                        style = TextStyle(
                            fontSize = 20.sp, // Bigger bullet size
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Display the ingredient text
                    Text (
                        text = instruction,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error: Unable to load recipe details.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

