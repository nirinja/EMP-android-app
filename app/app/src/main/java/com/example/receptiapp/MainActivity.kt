package com.example.receptiapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
private var searchtemp = ""

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivityLifecycle"
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private lateinit var lightSensorListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        saved = intent.getStringExtra("SAVED") ?: ""
        search = intent.getStringExtra("SEARCH") ?: ""

        // Initialize SensorManager and Light Sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        var isDarkTheme by mutableStateOf(false)

        lightSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val lightLevel = event?.values?.get(0) ?: 0f
                // Switch theme based on light level
                isDarkTheme = lightLevel < 70 // Example threshold for switching themes
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        setContent {
            com.example.receptiapp.ui.theme.ReceptiAppTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen(
                        onSearchClick = {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("SEARCH", searchtemp)
                            startActivity(intent)
                        },
                        onSavedClick = {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("SAVED", "SAVED")
                            startActivity(intent)
                        }
                    )
                }
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
        lightSensor?.let {
            sensorManager.registerListener(
                lightSensorListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(lightSensorListener)
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Meal Type Filter
                FilterWithLabel(
                    label = "Meal type:",
                    dropdownLabel = selectedMealType,
                    options = mealTypes,
                    dropdownExpanded = mealTypeDropdownExpanded,
                    onDropdownToggle = { mealTypeDropdownExpanded = it },
                    onOptionSelected = {
                        selectedMealType = it
                        mealTypeDropdownExpanded = false
                    }
                )

                // Difficulty Filter
                FilterWithLabel(
                    label = "Difficulty:",
                    dropdownLabel = selectedDifficulty,
                    options = difficulties,
                    dropdownExpanded = difficultyDropdownExpanded,
                    onDropdownToggle = { difficultyDropdownExpanded = it },
                    onOptionSelected = {
                        selectedDifficulty = it
                        difficultyDropdownExpanded = false
                    }
                )
            }
            searchtemp = searchQuery;
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
                                (selectedDifficulty == "All" || it.difficulty.equals(
                                    selectedDifficulty,
                                    ignoreCase = true
                                )) &&
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
fun FilterWithLabel(
    label: String,
    dropdownLabel: String,
    options: List<String>,
    dropdownExpanded: Boolean,
    onDropdownToggle: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 4.dp, top = 6.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(end = 8.dp)
        )
        FilterDropdown(
            label = dropdownLabel,
            options = options,
            dropdownExpanded = dropdownExpanded,
            onDropdownToggle = onDropdownToggle,
            onOptionSelected = onOptionSelected
        )
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
                color = Color.LightGray.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
            )
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onDropdownToggle(true) }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
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
fun BottomMenuBar(onSavedClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp) // Height of the bar
            .padding(horizontal = 16.dp) // Spacing from the sides
            .offset(y = (-80).dp) // Adjust position
    ) {
        IconButton(
            onClick = onSavedClick,
            modifier = Modifier
                .align(Alignment.CenterEnd) // Align to the bottom-right
                .padding(end = 30.dp) // Spacing from the right edge
                .shadow(8.dp, RoundedCornerShape(50.dp)) // Shadow effect
                .background(
                    color = Color.Red.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(50.dp) // Circular background
                )
                .size(70.dp) // Increase size of the button to make the circle bigger
        ) {
            Icon(
                painter = painterResource(id = R.drawable.saved),
                contentDescription = "Saved Recipes",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(30.dp) // icon size the same
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