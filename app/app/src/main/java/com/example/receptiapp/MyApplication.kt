package com.example.receptiapp

import android.app.Application
import androidx.room.Room
import com.example.receptiapp.data.AppDatabase

class MyApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}

