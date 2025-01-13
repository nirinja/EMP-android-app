package com.example.receptiapp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReceptiAppTheme {
                MainScreen(
                    onSearchClick = { startActivity(Intent(this, SearchActivity::class.java)) },
                    onSavedClick = { startActivity(Intent(this, SavedActivity::class.java)) }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onSavedClick: () -> Unit
) {
    var recipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        recipes = fetchRecipesData()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onSearchClick) {
                Text(text = "Search")
            }
            Button(onClick = onSavedClick) {
                Text(text = "Saved")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(recipes) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onClick = {
                        val intent = Intent(context, RecipeActivity::class.java).apply {
                            putExtra("RECIPE", JSONObject().apply {
                                put("name", recipe.name)
                                put("instructions", JSONArray(recipe.instructions))
                                put("ingredients", JSONArray(recipe.ingredients))
                                put("prepTimeMinutes", recipe.prepTime.toInt())
                                put("cookTimeMinutes", recipe.cookTime.toInt())
                            }.toString())
                        }
                        context.startActivity(intent)

                    }
                )
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            AsyncImage(
                model = recipe.image,
                contentDescription = recipe.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(brush =
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )))
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
                contentAlignment = Alignment.BottomStart) {
                Column {
                    Text(
                        recipe.name,
                        style = TextStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "Prep: ${recipe.prepTime} min | Cook: ${recipe.cookTime} min | ${recipe.difficulty}" ,
                        style = TextStyle(color = Color.LightGray, fontSize = 9.sp)
                    )
                }


            }
        }

    }
}

@Composable
fun ReceptiAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}

suspend fun fetchRecipesData(): List<Recipe> {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL("https://dummyjson.com/recipes")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().readText()
                val jsonArray = JSONObject(response).getJSONArray("recipes")
                parseRecipes(jsonArray)
            } else emptyList()
        } catch (e: Exception) {
            Log.e("MainScreen", "Error fetching recipes: ${e.message}")
            emptyList()
        }
    }
}

fun parseRecipes(jsonArray: JSONArray): List<Recipe> {
    val recipes = mutableListOf<Recipe>()
    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        recipes.add(
            Recipe(
                name = jsonObject.getString("name"),
                ingredients = jsonObject.getJSONArray("ingredients").toList(),
                instructions = jsonObject.getJSONArray("instructions").toList(),
                prepTime = jsonObject.getInt("prepTimeMinutes"),
                cookTime = jsonObject.getInt("cookTimeMinutes"),
                difficulty = jsonObject.getString("difficulty"),
                image = jsonObject.getString("image")
            )
        )
    }
    return recipes
}

data class Recipe(
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTime: Int,
    val cookTime: Int,
    val difficulty: String,
    val image: String
)
