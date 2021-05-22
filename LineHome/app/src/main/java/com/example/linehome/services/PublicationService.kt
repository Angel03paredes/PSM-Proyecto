package com.example.linehome.services

import com.example.linehome.models.Post
import com.example.linehome.models.SearchPublication
import retrofit2.Call
import retrofit2.http.*

interface PublicationService {

    @GET("getPublications")
    fun getPublications(): Call<List<Post>>

    @GET("getPublicationById/{id}")
    fun getPublicationById(@Path("id") id: Int): Call<Post>

    @GET("getPublicationByOwner/{id}")
    fun getPublicationByOwner(@Path("id") id: Int): Call<List<Post>>

    @Headers( "Content-Type: application/json")
    @POST("addPublication")
    fun addPublication(@Body publication:Post): Call<Post>

    @Headers( "Content-Type: application/json")
    @POST("getSearchPublication")
    fun getSearchPublication(@Body search: SearchPublication): Call<List<Post>>

}