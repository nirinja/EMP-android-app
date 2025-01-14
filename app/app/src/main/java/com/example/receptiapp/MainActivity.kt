package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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


// Fixes applied to MainScreen and DropdownMenuItem usage
@Composable
fun MainScreen(
    onSearchClick: (String) -> Unit,
    onSavedClick: () -> Unit
) {
    var recipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("All") }
    var selectedDifficulty by remember { mutableStateOf("All") }
    var mealTypeDropdownExpanded by remember { mutableStateOf(false) }
    var difficultyDropdownExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val mealTypes = listOf("All", "Dinner", "Snack", "Breakfast", "Lunch", "Dessert")
    val difficulties = listOf("All", "Easy", "Medium", "Hard")

    LaunchedEffect(Unit) {
        if (saved.equals("SAVED")) {
            recipes = loadRecipesFromDatabase()
        } else {
            recipes = fetchRecipesData()
        }
    }

    Scaffold(
        bottomBar = {
            BottomMenuBar(onSavedClick = onSavedClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search recipes...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { onSearchClick(searchQuery) }) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_search),
                            contentDescription = "Search Icon"
                        )
                    }
                }
            )

            // Filters Row: Meal Type and Difficulty Dropdowns
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Meal Type Dropdown
                FilterDropdown(
                    label = selectedMealType,
                    options = mealTypes,
                    dropdownExpanded = mealTypeDropdownExpanded,
                    onDropdownToggle = { mealTypeDropdownExpanded = it },
                    onOptionSelected = {
                        selectedMealType = it
                        mealTypeDropdownExpanded = false
                    }
                )

                // Difficulty Dropdown
                FilterDropdown(
                    label = selectedDifficulty,
                    options = difficulties,
                    dropdownExpanded = difficultyDropdownExpanded,
                    onDropdownToggle = { difficultyDropdownExpanded = it },
                    onOptionSelected = {
                        selectedDifficulty = it
                        difficultyDropdownExpanded = false
                    }
                )
            }

            // Recipes Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    recipes.filter {
                        (selectedMealType == "All" || it.mealType.contains(selectedMealType)) &&
                                (selectedDifficulty == "All" || it.difficulty.equals(selectedDifficulty, ignoreCase = true)) &&
                                it.name.contains(searchQuery, ignoreCase = true)
                    }
                ) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onClick = {
                            val intent = Intent(context, RecipeActivity::class.java).apply {
                                putExtra("RECIPE", JSONObject().apply {
                                    put("name", recipe.name)
                                    put("instructions", JSONArray(recipe.instructions))
                                    put("ingredients", JSONArray(recipe.ingredients))
                                    put("prepTimeMinutes", recipe.prepTime)
                                    put("cookTimeMinutes", recipe.cookTime)
                                    put("difficulty", recipe.difficulty)
                                    put("image", recipe.image)
                                    put("mealType", JSONArray(recipe.mealType))
                                }.toString())
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FilterDropdown(
    label: String,
    options: List<String>,
    dropdownExpanded: Boolean,
    onDropdownToggle: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onDropdownToggle(true) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )

        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { onDropdownToggle(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                    },
                    text = {
                        Text(
                            text = option,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun BottomMenuBar(onSavedClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp) // Thinner height for the bar
            .padding(horizontal = 16.dp) // Add spacing around the bar
            .offset(y = (-60).dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)) // Shadow and rounded corners
            .background(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(16.dp) // Rounded corners
            )
    ) {
        IconButton(
            onClick = onSavedClick,
            modifier = Modifier
                .align(Alignment.CenterEnd) // Align the icon to the right
                .padding(end = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.saved),
                contentDescription = "Saved Recipes",
                tint = MaterialTheme.colorScheme.onPrimary


            )
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

            // Meal type tags - aligned to the right edge with each tag only as wide as needed
            if (recipe.mealType.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd) // Align Column to the top-right corner
                        .padding(8.dp)
                ) {
                    recipe.mealType.forEach { mealType ->
                        Box(
                            modifier = Modifier
                                .padding(bottom = 4.dp)
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
                                text = mealType, // Display each meal type as a separate tag
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
                    mealType = toListR(recipe.mealType),
                    categoryId = null
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
                mealType = jsonObject.getJSONArray("mealType").toList(),
                categoryId = null
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
    val mealType: List<String>,
    val categoryId: Int?
)