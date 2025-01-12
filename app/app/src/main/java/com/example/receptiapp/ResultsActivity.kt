package com.example.receptiapp
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.receptiapp.databinding.ActivityResultsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding
    private var iskanoText: String? = null
    private var sestavineText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.iscibtn.setOnClickListener {
            val intent = Intent(this@ResultsActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.savedBtn.setOnClickListener {
            val intent = Intent(this@ResultsActivity, SavedActivity::class.java)
            startActivity(intent)
        }

        iskanoText =
            intent.getStringExtra("iskano")?.lowercase()?.replaceFirstChar { it.uppercase() } ?: ""
        //sestavine todo
        sestavineText = intent.getStringExtra("sestavine") ?: ""
        fetchRecipesData()
    }

    private fun fetchRecipesData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://dummyjson.com/recipes/search?q=$iskanoText")
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

                            // STEP: set an OnClickListener for this item
                            itemView.setOnClickListener {
                                val intent = Intent(this@ResultsActivity, RecipeActivity::class.java)
                                intent.putExtra("RECIPE", recipeObj.toString())
                                startActivity(intent)
                            }

                            container.addView(itemView)
                        }
                    }


                }
            } catch (e: SecurityException) {
                println("Security Exception: ${e.message}")
            } catch (e: Exception) {
                println("General Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}