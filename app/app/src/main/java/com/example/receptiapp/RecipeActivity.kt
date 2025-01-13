package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.receptiapp.data.Recipe
import com.example.receptiapp.databinding.ActivityRecipeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private var receptText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        // Nastavitev WindowInsets (prepričaj se, da imaš pravilen id)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recipeConstraintLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.iscibtn.setOnClickListener {
            val intent = Intent(this@RecipeActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        // Ko kliknemo na gumb za shranjevanje, shranimo ali izbrišemo recept v/iz baze in preusmerimo v SavedActivity
        binding.savedBtn.setOnClickListener {
            saveRecipeToDatabase()
        }

        // Dobimo JSON podatek za prikaz recepta
        receptText = intent.getStringExtra("RECIPE")
        val myRecipe = JSONObject(receptText)

        // Pridobivanje podatkov iz JSON objekta
        val name = myRecipe.getString("name")
        val instructionsJson = myRecipe.getJSONArray("instructions")
        val ingredientsJson = myRecipe.getJSONArray("ingredients")
        val prepTimeMinutes = myRecipe.getInt("prepTimeMinutes")
        val cookTimeMinutes = myRecipe.getInt("cookTimeMinutes")

        // Prikaz podatkov – po potrebi prilagodi, kako združiš podatke
        CoroutineScope(Dispatchers.Main).launch {
            binding.imeJedi.text = name
            binding.navodila.text = instructionsJson.join(" ") // Prikaz navodil kot en niz
            binding.setavine.text = ingredientsJson.join(" ")    // Prikaz sestavin kot en niz
            binding.cookTime.text = "$prepTimeMinutes + $cookTimeMinutes min"
        }
    }

    private fun saveRecipeToDatabase() {
        receptText?.let {
            val myRecipe = JSONObject(it)
            val name = myRecipe.getString("name")
            val instructionsJson = myRecipe.getJSONArray("instructions")
            // Ustvarimo opis, združimo sestavine in podatke o časih
            val description = "Sestavine: " + myRecipe.getJSONArray("ingredients").join(" ") +
                    "\nČas priprave: ${myRecipe.getInt("prepTimeMinutes")} min" +
                    "\nČas kuhanja: ${myRecipe.getInt("cookTimeMinutes")} min"
            val instructions = instructionsJson.join(" ")

            // Ustvarimo objekt Recipe
            val recipe = Recipe(
                recipeId = 0, // pri autoGenerate ni potrebno podajati ID-ja
                name = name,
                description = description,
                instructions = instructions,
                imageUrl = null,  // če nimaš slike, pusti null
                categoryId = null // če ne uporabljaš kategorij, naj bo null
            )

            // Shranjevanje ali brisanje v ozadni niti
            CoroutineScope(Dispatchers.IO).launch {
                // Preverimo, če recept z istim imenom že obstaja
                val existingRecipe = MyApplication.database.recipeDao().getRecipeByName(name)
                if (existingRecipe != null) {
                    // Če recept obstaja, ga izbrišemo iz baze
                    MyApplication.database.recipeDao().deleteRecipe(existingRecipe)
                } else {
                    // Če recepta še ni, ga dodamo v bazo
                    MyApplication.database.recipeDao().insertRecipe(recipe)
                }
            }
        }
    }
}
