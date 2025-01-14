package com.example.receptiapp

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.receptiapp.databinding.ActivitySearchBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val zajtrkBox = findViewById<CheckBox>(R.id.tip1)
        val kosiloBox = findViewById<CheckBox>(R.id.tip2)
        val vecerjaBox = findViewById<CheckBox>(R.id.tip3)

        val tezavnost1Box = findViewById<CheckBox>(R.id.teza1)
        val tezavnost2Box = findViewById<CheckBox>(R.id.teza2)
        val tezavnost3Box = findViewById<CheckBox>(R.id.teza3)


        val casBar = findViewById<SeekBar>(R.id.seekBar)

        val jajcaBox = findViewById<CheckBox>(R.id.jajca)
        val mokaBox = findViewById<CheckBox>(R.id.moka)
        val mlekoBox = findViewById<CheckBox>(R.id.mleko)
        val oresckiBox = findViewById<CheckBox>(R.id.orescki)
        val veganBox = findViewById<CheckBox>(R.id.vegan)
        val vegetaBox = findViewById<CheckBox>(R.id.vegeta)
        val morskoBox = findViewById<CheckBox>(R.id.morsko)
        val sojaBox = findViewById<CheckBox>(R.id.soja)
        val halalBox = findViewById<CheckBox>(R.id.halal)

        val iskano = findViewById<EditText>(R.id.isci)
        val sestavine = findViewById<EditText>(R.id.sestavineText)
        val isciBtn = findViewById<FloatingActionButton>(R.id.iscibtn)


        isciBtn.setOnClickListener {
            val izbraniFiltri = listOf(
                zajtrkBox.takeIf { it.isChecked }?.text,
                kosiloBox.takeIf { it.isChecked }?.text,
                vecerjaBox.takeIf { it.isChecked }?.text,
                tezavnost1Box.takeIf { it.isChecked }?.text,
                tezavnost2Box.takeIf { it.isChecked }?.text,
                tezavnost3Box.takeIf { it.isChecked }?.text
            ).filterNotNull()

            val izbraneDiete = listOf(
                jajcaBox.takeIf { it.isChecked }?.text,
                mokaBox.takeIf { it.isChecked }?.text,
                mlekoBox.takeIf { it.isChecked }?.text,
                oresckiBox.takeIf { it.isChecked }?.text,
                veganBox.takeIf { it.isChecked }?.text,
                vegetaBox.takeIf { it.isChecked }?.text,
                morskoBox.takeIf { it.isChecked }?.text,
                sojaBox.takeIf { it.isChecked }?.text,
                halalBox.takeIf { it.isChecked }?.text
            ).filterNotNull()

        }


        isciBtn.setOnClickListener {
            val iskanoText = iskano.text.toString()
            val inputText = sestavine.text.toString()

            val intent = Intent(this@SearchActivity, MainActivity::class.java)
            intent.putExtra("SEARCH", iskanoText)
            intent.putExtra("FILTRI", inputText)
            startActivity(intent)
        }

        binding.savedBtn.setOnClickListener {
            val intent = Intent(this@SearchActivity, MainActivity::class.java)
            intent.putExtra("SAVED", "SAVED")
            startActivity(intent)
        }
    }
}