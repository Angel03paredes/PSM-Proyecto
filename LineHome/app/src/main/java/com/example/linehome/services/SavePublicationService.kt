package com.example.linehome.services

import com.example.linehome.models.Post
import com.example.linehome.models.SavePublication
import retrofit2.Call
import retrofit2.http.*

interface SavePublicationService {

    @Headers( "Content-Type: application/json")
    @POST("addSavePublication")
    fun addSavePublication(@Body savePublication: SavePublication ): Call<SavePublication>

    @Headers( "Content-Type: application/json")
    @POST("deleteSavePublication")
    fun deleteSavePublication(@Body savePublication: SavePublication ): Call<SavePublication>

    @GET("getSavePublicationsByUser/{userId}")
    fun getSavePublicationsByUser(@Path("userId") userId: Int): Call<List<Post>>

    @Headers( "Content-Type: application/json")
    @GET("getSavePublicationByUserAndPublication/{userId}/{publicationId}")
    fun getSavePublicationByUserAndPublication(@Path("userId") userId: Int, @Path("publicationId") publicationId: Int): Call<SavePublication>
}