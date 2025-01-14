package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.receptiapp.data.Recipe
import kotlinx.coroutines.launch
import org.json.JSONObject

class RecipeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

suspend fun saveRecipe(recipeData: JSONObject) {
    val recipe = Recipe(
        name = recipeData.getString("name"),
        ingredients = recipeData.getJSONArray("ingredients").toString(),
        instructions = recipeData.getJSONArray("instructions").toString(),
        prepTime = recipeData.getInt("prepTimeMinutes"),
        cookTime = recipeData.getInt("cookTimeMinutes"),
        difficulty = recipeData.getString("difficulty"),
        image = recipeData.getString("image"),
        mealType = recipeData.getJSONArray("mealType").toString(),
        categoryId = null
    )

    val recipeDao = MyApplication.database.recipeDao()
    recipeDao.insertRecipe(recipe)
}

@Composable
fun RecipeScreen(recipeJson: String, onBackClick: () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val recipe = remember { JSONObject(recipeJson) }

    val name = recipe.getString("name")
    val ingredients = recipe.getJSONArray("ingredients")
    val instructions = recipe.getJSONArray("instructions")
    val prepTime = recipe.getInt("prepTimeMinutes")
    val cookTime = recipe.getInt("cookTimeMinutes")
    val difficulty = recipe.getString("difficulty")
    val imageUrl = recipe.getString("image")

    // State to track if the recipe is saved
    var isSaved by remember { mutableStateOf(false) }

    // Check if the recipe is saved when the screen is loaded
    LaunchedEffect(name) {
        val recipeDao = MyApplication.database.recipeDao()
        isSaved = recipeDao.getRecipeByName(name) != null
    }

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
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Recipe Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                )

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

            // Back button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.left_big),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            // Save/Unsave button
            IconButton(
                onClick = {
                    scope.launch {
                        val recipeDao = MyApplication.database.recipeDao()
                        if (isSaved) {
                            // Unsave the recipe
                            recipeDao.deleteRecipeByName(name)
                            isSaved = false
                            Toast.makeText(context, "Recipe removed from saved recipes.", Toast.LENGTH_SHORT).show()
                        } else {
                            // Save the recipe
                            saveRecipe(recipe)
                            isSaved = true
                            Toast.makeText(context, "Recipe saved successfully!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isSaved) R.drawable.saved else R.drawable.save
                    ),
                    contentDescription = if (isSaved) "Unsave Recipe" else "Save Recipe",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Recipe details (name, ingredients, instructions, etc.)
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = "Preparation: $prepTime min | Cook time: $cookTime | Difficulty: $difficulty",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Column(modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp)) {
            for (i in 0 until ingredients.length()) {
                val ingredient = ingredients.getString(i)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Text(
                        text = "•",
                        style = TextStyle(fontSize = 20.sp, color = Color.Black)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
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
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Text(
                        text = "•",
                        style = TextStyle(fontSize = 20.sp, color = Color.Black)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = instruction,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
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