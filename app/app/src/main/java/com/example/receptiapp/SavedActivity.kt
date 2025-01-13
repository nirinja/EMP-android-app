package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.receptiapp.databinding.ActivitySavedBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate binding and set content view once
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply insets to "main" view (ensure that the id exists in activity_saved.xml)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Button click listeners
        binding.iscibtn.setOnClickListener {
            val intent = Intent(this@SavedActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.savedBtn.setOnClickListener {
            val intent = Intent(this@SavedActivity, SavedActivity::class.java)
            startActivity(intent)
        }

        loadRecipesFromDatabase()
    }

    private fun loadRecipesFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val recipeDao = MyApplication.database.recipeDao()
            val recipesFromDb = recipeDao.getAllRecipes()
            // Preklopi na glavno nit za posodobitev UI
            launch(Dispatchers.Main) {
                val container = binding.recipesContainer
                container.removeAllViews()
                recipesFromDb.forEach { recipe ->
                    val itemView = layoutInflater.inflate(R.layout.item_recipe, container, false)
                    itemView.findViewById<TextView>(R.id.tvRecipeName).text = recipe.name
                    itemView.findViewById<TextView>(R.id.tvIngredients).text = recipe.description // ali ingrediente, če jih imaš
                    itemView.findViewById<TextView>(R.id.tvInstructions).text = recipe.instructions
                    // Nastavi drug UI podatke po potrebi

                    itemView.setOnClickListener {
                        val intent = Intent(this@SavedActivity, RecipeActivity::class.java)
                        // Po potrebi serializiraj recept (npr. z Gson ali enostavnim intent extra)
                        intent.putExtra("RECIPE", recipe.name)
                        startActivity(intent)
                    }
                    container.addView(itemView)
                }
            }
        }
    }


}
