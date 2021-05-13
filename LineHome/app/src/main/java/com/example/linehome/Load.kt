package com.example.linehome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.linehome.models.User
import com.example.linehome.services.RestEngine
import com.example.linehome.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Load : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)


    }

    private fun logIn() {
        val activityLogIn = Intent(this,MainActivity::class.java)
        startActivity(activityLogIn)
    }

    fun action(){

        val activityLogIn = Intent(this,HomeActivity::class.java)
        startActivity(activityLogIn)
    }

     override fun onResume(){
         super.onResume()
        val sharedPreferences : SharedPreferences = getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email","")
        val password = sharedPreferences.getString("password","")

        if(!email.toString().isEmpty() && !password.toString().isEmpty()){
            action()
        }else{
            logIn()
        }
    }

}