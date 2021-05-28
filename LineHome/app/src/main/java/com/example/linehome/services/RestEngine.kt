package com.example.linehome.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RestEngine {

    companion object {
        fun getRestEngine() : Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client =  OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                    .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                    .readTimeout(5, TimeUnit.MINUTES) // read timeou
                    .build()
            val retrofit =  Retrofit.Builder()
                //.baseUrl("http://167.99.10.71:3050/api/") // tu url
                .baseUrl("http://localhost:3050/api/") // local
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()




            return retrofit
        }
    }

}