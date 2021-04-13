package com.example.linehome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnIniciarSesion.setOnClickListener {
            showLogin()
        }

        btnRegistrarte.setOnClickListener {
            showRegister()
        }
    }

    private fun showLogin() {
        val activityLogin = Intent(this, LoginActivity::class.java)
        startActivity(activityLogin)
    }

    private fun showRegister() {
        val activityRegister = Intent(this, RegisterActivity::class.java)
        startActivity(activityRegister)
    }
}