package com.example.linehome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.btnOnBack
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        btnOnBack.setOnClickListener{
            showHome()
        }

        btnLogout.setOnClickListener {
            showStart()
        }
    }

    private fun showHome() {
        val activityHome = Intent(this, HomeActivity::class.java)
        startActivity(activityHome)
        finish()
    }

    private fun showStart() {
        val activityHome = Intent(this, MainActivity::class.java)
        startActivity(activityHome)
        finish()
    }
}