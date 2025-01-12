package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.receptiapp.databinding.ActivityRecipeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private var receptText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        // Use the correct view id from your layout file
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recipeConstraintLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.iscibtn.setOnClickListener {
            val intent = Intent(this@RecipeActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.savedBtn.setOnClickListener {
            val intent = Intent(this@RecipeActivity, SavedActivity::class.java)
            startActivity(intent)
        }


        // Remove spaces from the received recipe name
        receptText = intent.getStringExtra("RECIPE")
        val myRecipe = JSONObject(receptText)

        val name = myRecipe.getString("name")
        val instructions = myRecipe.getJSONArray("instructions")
        val ingredients = myRecipe.getJSONArray("ingredients")
        val prepTimeMinutes = myRecipe.getInt("prepTimeMinutes")
        val cookTimeMinutes = myRecipe.getInt("cookTimeMinutes")

        // Switch to Main Thread to update UI:
        CoroutineScope(Dispatchers.Main).launch {
            binding.imeJedi.text = name
            binding.navodila.text =
                instructions.join(" ")  // Customize how you display instructions.
            binding.setavine.text =
                ingredients.join(" ")     // Customize how you display ingredients.
            binding.cookTime.text = "$prepTimeMinutes + $cookTimeMinutes min"
        }
    }
}
