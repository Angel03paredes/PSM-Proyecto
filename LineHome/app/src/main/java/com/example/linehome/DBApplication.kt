package com.example.linehome

import android.app.Application
import com.example.linehome.database.DataDBHelper

class DBApplication: Application() {

    companion object{
        lateinit var dataDBHelper: DataDBHelper
    }

    override fun onCreate() {
        super.onCreate()
        dataDBHelper =  DataDBHelper(applicationContext)
    }
}