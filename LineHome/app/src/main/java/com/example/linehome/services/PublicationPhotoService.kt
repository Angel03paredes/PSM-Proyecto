package com.example.linehome.services

import com.example.linehome.models.PublicationPhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface PublicationPhotoService {

    @Headers( "Content-Type: application/json")
    @GET("getFirstImageByPublication/{id}")
    fun getFirstImageByPublication(@Path("id") id: Int): Call<PublicationPhoto>
}