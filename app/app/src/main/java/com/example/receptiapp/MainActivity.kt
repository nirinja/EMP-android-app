package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.receptiapp.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivityLifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        // Apply insets to mainConstraintLayout
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainConstraintLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.iscibtn.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.savedBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, SavedActivity::class.java)
            startActivity(intent)
        }

        fetchRecipesData()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() klican")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() klican")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() klican")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() klican")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart() klican")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() klican")
    }

    private fun fetchRecipesData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://dummyjson.com/recipes")
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                if (connection.responseCode == 200) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(response)
                    val recipesArray = jsonObject.getJSONArray("recipes")

                    val count = minOf(10, recipesArray.length())

                    // Switch to the main thread to update UI
                    launch(Dispatchers.Main) {
                        val container = binding.recipesContainer
                        container.removeAllViews()

                        for (i in 0 until count) {
                            val recipeObj = recipesArray.getJSONObject(i)
                            val name = recipeObj.getString("name")
                            val instructions = recipeObj.getString("instructions")
                            val ingredients = recipeObj.getString("ingredients")
                            val prepTime = recipeObj.getString("prepTimeMinutes")
                            val cookTime = recipeObj.getString("cookTimeMinutes")

                            // Inflate the single-recipe layout
                            val itemView = layoutInflater.inflate(R.layout.item_recipe, container, false)
                            // Populate it
                            itemView.findViewById<TextView>(R.id.tvRecipeName).text = name
                            itemView.findViewById<TextView>(R.id.tvIngredients).text = ingredients
                            itemView.findViewById<TextView>(R.id.tvInstructions).text = instructions
                            itemView.findViewById<TextView>(R.id.tvTimes).text = "$prepTime min + $cookTime min"

                            // Set an OnClickListener for this item
                            itemView.setOnClickListener {
                                val intent = Intent(this@MainActivity, RecipeActivity::class.java)
                                intent.putExtra("RECIPE", recipeObj.toString())
                                startActivity(intent)
                            }

                            container.addView(itemView)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Prišlo je do izjeme: ${e.message}")
            }
        }
    }
}
