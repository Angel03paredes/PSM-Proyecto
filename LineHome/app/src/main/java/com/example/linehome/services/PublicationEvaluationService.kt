package com.example.linehome.services

import com.example.linehome.models.Evaluation
import com.example.linehome.models.EvaluationPreview
import com.example.linehome.models.PublicationPhoto
import retrofit2.Call
import retrofit2.http.*

interface PublicationEvaluationService {

    @GET("getEvaluationByPublication/{id}")
    fun getEvaluationByPublication(@Path("id") id: Int): Call<EvaluationPreview>


    @GET("getEvaluationByUserAndPublication/{publicationId}/{userId}")
    fun getEvaluationByUserAndPublication(@Path ("publicationId")publicationId:Int,
                                            @Path("userId")userId:Int
    ): Call<Evaluation>

    @Headers( "Content-Type: application/json")
    @POST("addEvaluation")
    fun addEvaluation(@Body evaluation: Evaluation): Call<Evaluation>

}