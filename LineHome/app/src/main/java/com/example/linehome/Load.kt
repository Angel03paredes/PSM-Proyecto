package com.example.linehome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.linehome.DBApplication.Companion.dataDBHelper
import com.example.linehome.models.Post
import com.example.linehome.models.PublicationPhoto
import com.example.linehome.services.PublicationPhotoService
import com.example.linehome.services.PublicationService
import com.example.linehome.services.RestEngine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Load : AppCompatActivity() {

    var INTERNET_AVAILABLE : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

      uploadPublications()


    }

    private fun uploadPublications() {
        INTERNET_AVAILABLE = isNetDisponible()

        if(INTERNET_AVAILABLE){
            val posts : List<Post> = dataDBHelper.getPublicationsNotUpload()
            var idPublication :  Int = 0
            for(post in posts){

                val  imageList : List<ByteArray> = dataDBHelper.getPublicationsPhoto(post.id!!)

                val publicationService : PublicationService = RestEngine.getRestEngine().create(PublicationService::class.java)
                val result: Call<Post> = publicationService.addPublication(post)

                result.enqueue(object : Callback<Post> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        val item = response.body()

                        if (item != null) {
                            idPublication = item.id!!
                            for (image in imageList){
                                if (idPublication != null) {
                                    savePublicationPhotoApi(idPublication,image)
                                }
                            }

                        }
                        dataDBHelper.updatePublicationUpload()
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        print("Error: " + t)
                    }

                })
            }



        }
    }

    private fun logIn() {
        val activityLogIn = Intent(this, MainActivity::class.java)
        startActivity(activityLogIn)
    }

    fun action(){

        val activityLogIn = Intent(this, HomeActivity::class.java)
        startActivity(activityLogIn)
    }

     override fun onResume(){
         super.onResume()
        val sharedPreferences : SharedPreferences = getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")

        if(!email.toString().isEmpty() && !password.toString().isEmpty()){
            action()
        }else{
            logIn()
        }
    }

    private fun isNetDisponible(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetInfo = connectivityManager.activeNetworkInfo
        return actNetInfo != null && actNetInfo.isConnected
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun savePublicationPhotoApi(publication: Int, image: ByteArray) {

        val encodedString:String =  Base64.getEncoder().encodeToString(image)

        val publicationPhotoService : PublicationPhotoService = RestEngine.getRestEngine().create(PublicationPhotoService::class.java)
        val result: Call<PublicationPhoto> = publicationPhotoService.addPublicationPhoto(PublicationPhoto(null,publication,encodedString))

        result.enqueue(object : Callback<PublicationPhoto> {
            override fun onResponse(call: Call<PublicationPhoto>, response: Response<PublicationPhoto>) {
                print(response)
            }

            override fun onFailure(call: Call<PublicationPhoto>, t: Throwable) {
                print("Error: " + t)
            }

        })
    }

}