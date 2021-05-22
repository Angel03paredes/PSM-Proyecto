package com.example.linehome.services

import com.example.linehome.models.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("getUserById/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @Headers( "Content-Type: application/json")
    @POST("getUserByUserOrEmailAndPassword")
    fun getUserByUserOrEmailAndPassword(@Body user: User): Call<User>

    @Headers( "Content-Type: application/json")
    @POST("addUser")
    fun addUser(@Body user: User): Call<User>

    @Headers( "Content-Type: application/json")
    @POST("uploadImage/{id}")
    fun updateImageUser(@Path("id") id: Int, @Body user: User): Call<String>
}