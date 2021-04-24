package com.example.linehome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        imgProfile.setOnClickListener {
            showActivityProfile();
        }

        btnOnBack.setOnClickListener{
           finish()
        }

        btnAddPost.setOnClickListener {
            ShowCMS()
        }

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.saveFragment, R.id.searchFragment, R.id.chatFragment, R.id.notificationFragment))
        bottomNavigationView.setupWithNavController(findNavController(R.id.fragment))
    }

    private fun ShowCMS() {
        var activityCMS = Intent(this,CMSActivity::class.java)
        startActivity(activityCMS);
        finish()
    }

    private fun showActivityProfile() {
        var activityProfile = Intent(this, ProfileActivity::class.java)
        startActivity(activityProfile);
        finish()
    }

}