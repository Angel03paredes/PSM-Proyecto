package com.example.linehome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.linehome.models.User
import com.example.linehome.services.RestEngine
import com.example.linehome.services.UserService
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLoginClose.setOnClickListener {
            finish()
        }

        btnContact.setOnClickListener {
            val userEmail = editTextLoginUE.text.toString()
            val password = editTextLoginPassword.text.toString()

            if(userEmail.isNotEmpty() && password.isNotEmpty()) {
                getUserLogin(userEmail, password)
            }
            //showHome()
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

    private fun getUserLogin(emailUser: String, password: String) {
        //val user = User(null, emailUser, emailUser, password, null)
        val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
        val result: Call<User> = userService.getUserById(1)

        result.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val item = response.body()
                if(item != null) {
                    println(item)
                    println(item.userName)
                    println(item.email)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println(t.toString())
            }
        })
    }

}