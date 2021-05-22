package com.example.linehome.services

import com.example.linehome.models.Notification
import com.example.linehome.models.NotificationPreview
import com.example.linehome.models.Post
import retrofit2.Call
import retrofit2.http.*

interface NotificationService {

    @Headers( "Content-Type: application/json")
    @POST("addNotification")
    fun addNotification(@Body notification: Notification): Call<Notification>

    @Headers( "Content-Type: application/json")
    @POST("deleteNotification/{id}")
    fun deleteNotification(@Path("id") id: Int): Call<Notification>

    @GET("getNotificationsByReceiver/{receiverId}")
    fun getNotificationsByReceiver(@Path("receiverId") receiverId: Int): Call<List<NotificationPreview>>
}