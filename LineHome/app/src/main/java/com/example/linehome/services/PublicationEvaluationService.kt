package com.example.linehome.services

import com.example.linehome.models.EvaluationPreview
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PublicationEvaluationService {

    @GET("getEvaluationByPublication/{id}")
    fun getEvaluationByPublication(@Path("id") id: Int): Call<EvaluationPreview>

}