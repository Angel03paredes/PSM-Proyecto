package com.example.linehome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.linehome.models.User
import com.example.linehome.services.RestEngine
import com.example.linehome.services.UserService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
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

        btnRegisterRegistrar.setOnClickListener {
            addUser()
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

    private fun addUser(){
        val name:String = editTextRegisterUsuario.text.toString()
        val email:String = editTextRegisterEmail.text.toString()
        val pass:String = editTextRegisterPassword.text.toString()

        if(name.isEmpty() && email.isEmpty() && pass.isEmpty()){
            Toast.makeText(this,"Llene todos los campos",Toast.LENGTH_LONG).show()
        }
        else{
            val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
            val result: Call<User> = userService.addUser(User(null,name,email,pass,null))

            result.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val item = response.body()
                    if(item != null) {

                        action(item)

                    }else{
                        Toast.makeText(this@RegisterActivity,"No sse ha podido registrar",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    println(t.toString())
                }
            })
        }
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