package com.example.linehome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.saveFragment, R.id.searchFragment, R.id.chatFragment, R.id.notificationFragment))
        bottomNavigationView.setupWithNavController(findNavController(R.id.fragment))
    }
}