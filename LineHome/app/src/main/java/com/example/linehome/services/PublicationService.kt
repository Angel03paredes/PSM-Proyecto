package com.example.linehome.services

import com.example.linehome.models.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PublicationService {

    @GET("getPublications")
    fun getPublications(): Call<List<Post>>

    @GET("getPublicationById/{id}")
    fun getPublicationById(@Path("id") id: Int): Call<Post>
}