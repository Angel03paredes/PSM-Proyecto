package com.example.linehome.services

import com.example.linehome.models.Post
import com.example.linehome.models.PublicationPhoto
import retrofit2.Call
import retrofit2.http.*

interface PublicationPhotoService {

    @Headers( "Content-Type: application/json")
    @GET("getFirstImageByPublication/{id}")
    fun getFirstImageByPublication(@Path("id") id: Int): Call<PublicationPhoto>

    @Headers( "Content-Type: application/json")
    @POST("addPublicationPhoto")
    fun addPublicationPhoto(@Body publicationPhoto: PublicationPhoto): Call<PublicationPhoto>

    @Headers( "Content-Type: application/json")
    @GET("getPublicationPhotoByPublicationId/{id}")
    fun getPublicationPhotoByPublicationId(@Path("id") id: Int): Call<List<PublicationPhoto>>

}