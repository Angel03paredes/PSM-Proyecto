package com.example.linehome.services

import com.example.linehome.models.Post
import retrofit2.Call
import retrofit2.http.*

interface PublicationService {

    @GET("getPublications")
    fun getPublications(): Call<List<Post>>

    @GET("getPublicationById/{id}")
    fun getPublicationById(@Path("id") id: Int): Call<Post>

    @Headers( "Content-Type: application/json")
    @POST("addPublication")
    fun addPublication(@Body publication:Post): Call<Post>

}