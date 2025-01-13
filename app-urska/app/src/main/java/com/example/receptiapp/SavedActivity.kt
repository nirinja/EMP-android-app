package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.receptiapp.databinding.ActivitySavedBinding

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
    }
}
