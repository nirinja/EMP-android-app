package com.example.receptiapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.receptiapp.databinding.ActivityMain2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private var iskanoText: String? = null
    private var sestavineText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
                val url = URL("https://www.themealdb.com/api/json/v1/1/search.php?s=$iskanoText")
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                if (connection.responseCode == 200) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(response)
                    val recipesArray = jsonObject.getJSONArray("meals")
                    val recipe = recipesArray.getJSONObject(0)
                    val strInstructions = recipe.getString("strInstructions")
                    val strMeal = recipe.getString("strMeal")

                    // Posodobi glavno nit
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.imeJedi.text = strMeal
                        binding.navodila.text = strInstructions
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
