package com.example.linehome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegisterClose.setOnClickListener {
            finish()
        }

        btnRegisterRegistrar.setOnClickListener {
            showHome()
        }

        txtActivityLogIn.setOnClickListener{
            showLogIn()
        }
    }

    private fun showHome() {
        val activityHome = Intent(this, HomeActivity::class.java)
        startActivity(activityHome)
        finish()
    }

    private fun showLogIn(){
        val activityLogIn = Intent(this,LoginActivity::class.java)
        startActivity(activityLogIn)
        finish()
    }
}