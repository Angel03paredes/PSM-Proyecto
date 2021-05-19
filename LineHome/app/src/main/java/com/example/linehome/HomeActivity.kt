package com.example.linehome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.btnOnBack
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var imageBs : String
    @RequiresApi(Build.VERSION_CODES.O)
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

        showImageProfile()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showImageProfile() {
        val sharedPreferences : SharedPreferences = getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        imageBs = sharedPreferences.getString("imageUrl","").toString()
        if(imageBs != null){
            val imageBytes = Base64.getDecoder().decode(imageBs)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imgProfile.setImageBitmap(decodedImage)
        }
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