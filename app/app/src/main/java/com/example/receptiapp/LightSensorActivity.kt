package com.example.receptiapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.receptiapp.ui.theme.ReceptiAppTheme

class LightSensorActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private lateinit var lightSensorListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SensorManager and Light Sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        var isDarkTheme by mutableStateOf(false)

        lightSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val lightLevel = event?.values?.get(0) ?: 0f
                // Switch theme based on light level
                isDarkTheme = lightLevel < 50 // Example threshold for switching themes
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        setContent {
            ReceptiAppTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Your UI goes here
                    MainScreen(
                        onSearchClick = { /* Handle search */ },
                        onSavedClick = { /* Handle saved recipes */ }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.let {
            sensorManager.registerListener(lightSensorListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(lightSensorListener)
    }
}
