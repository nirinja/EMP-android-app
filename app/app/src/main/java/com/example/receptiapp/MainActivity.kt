package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


private var saved = ""
private var search = ""
private var filtri = ""

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivityLifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        saved = intent.getStringExtra("SAVED") ?: ""
        search = intent.getStringExtra("SEARCH") ?: ""
        filtri = intent.getStringExtra("FILTRI") ?: ""
        setContent {
            ReceptiAppTheme {
                MainScreen(
                    onSearchClick = { startActivity(Intent(this, SearchActivity::class.java)) },
                    onSavedClick = {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("SAVED", "SAVED")
                        startActivity(intent)
                    }
                )
            }
        }
        Log.d(TAG, "onCreate() called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
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
        if (saved.equals("SAVED")) {
            recipes = loadRecipesFromDatabase()
        } else {
            recipes = fetchRecipesData()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 300f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Text(
                        recipe.name,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        "Prep: ${recipe.prepTime} min | Cook: ${recipe.cookTime} min | ${recipe.difficulty}",
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

suspend fun loadRecipesFromDatabase(): List<Recipe> {
    return withContext(Dispatchers.IO) {
        try {
            val recipeDao = MyApplication.database.recipeDao()
            val recipesFromDb = recipeDao.getAllRecipes()
            recipesFromDb.map { recipe ->
                Recipe(
                    name = recipe.name,
                    ingredients = toListR(recipe.ingredients),
                    instructions = toListR(recipe.instructions),
                    prepTime = recipe.prepTime,
                    cookTime = recipe.cookTime,
                    difficulty = recipe.difficulty,
                    image = recipe.image,
                    mealType = toListR(recipe.mealType)
                )
            }
        } catch (e: Exception) {
            Log.e("loadRecipesFromDatabase", "Error fetching recipes: ${e.message}")
            emptyList()
        }
    }
}

fun toListR(value: String): List<String> {
    return value.split(",").map { it.trim() }
}


suspend fun fetchRecipesData(): List<Recipe> {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL("https://dummyjson.com/recipes/search?q=$search")
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
                image = jsonObject.getString("image"),
                mealType = jsonObject.getJSONArray("mealType").toList()
            )
        )
    }
    return recipes
}

fun JSONArray.toList(): List<String> {
    return List(length()) { i -> getString(i) }
}

data class Recipe(
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val prepTime: Int,
    val cookTime: Int,
    val difficulty: String,
    val image: String,
    val mealType: List<String>
)