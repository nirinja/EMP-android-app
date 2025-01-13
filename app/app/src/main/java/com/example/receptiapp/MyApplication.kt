package com.example.receptiapp

import android.app.Application
import androidx.room.Room
import com.example.receptiapp.data.AppDatabase

class MyApplication : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize your Room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "recipe_database"
        ).build()
    }
}
