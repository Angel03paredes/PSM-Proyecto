package com.example.linehome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLoginClose.setOnClickListener {
            finish()
        }

        btnContact.setOnClickListener {
            showHome()
        }

        txtActivitySignUp.setOnClickListener {
            showSignUp()
        }

    }

    private fun showHome() {
        val activityHome = Intent(this, HomeActivity::class.java)
        startActivity(activityHome)
        finish()
    }

    private fun showSignUp(){
        val activitySignUp = Intent(this,RegisterActivity::class.java)
        startActivity(activitySignUp)
        finish()
    }

}