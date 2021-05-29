package com.example.linehome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.linehome.models.User
import com.example.linehome.services.RestEngine
import com.example.linehome.services.UserService
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

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
            } else {
                Toast.makeText(this,"Llene todos los campos",Toast.LENGTH_LONG).show()
            }
        }

        txtActivitySignUp.setOnClickListener {
            showSignUp()
        }

    }

    private fun showSignUp(){
        val activitySignUp = Intent(this,RegisterActivity::class.java)
        startActivity(activitySignUp)
        finish()
    }

    private fun getUserLogin(emailUser: String, password: String) {
        progressBarHor.visibility = View.VISIBLE
        val user = User(null, emailUser, emailUser, password, null)
        val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
        val result: Call<User> = userService.getUserByUserOrEmailAndPassword(user)

        result.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val item = response.body()
                if(item != null) {
                    progressBarHor.visibility = View.GONE
                    action(item)

                }else{
                    Toast.makeText(this@LoginActivity,"No se ha podido ingresar", Toast.LENGTH_LONG).show()
                    progressBarHor.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println(t.toString())
            }
        })
    }

    private fun action(item: User) {
        val activityHome = Intent(this,HomeActivity::class.java)
        sharedPreferences = getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("id",item.id.toString())
        editor.putString("userName",item.userName)
        editor.putString("email",item.email)
        editor.putString("password",item.password)
        editor.putString("imageUrl",item.imageUrl)
        editor.apply()
        startActivity(activityHome)
    }

}