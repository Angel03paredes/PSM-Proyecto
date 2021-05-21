package com.example.linehome

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.linehome.DBApplication.Companion.dataDBHelper
import com.example.linehome.models.User
import com.example.linehome.services.RestEngine
import com.example.linehome.services.UserService
import com.example.linehome.utilities.ImageUtilities
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.btnOnBack
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*


class ProfileActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userName:String
    private lateinit var idUser:String
    private lateinit var imageBs : String
    var imgArray:ByteArray? =  null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val sharedPreferences : SharedPreferences = getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        userName = sharedPreferences.getString("userName","").toString()
        imageBs = sharedPreferences.getString("imageUrl","").toString()
        idUser = sharedPreferences.getString("id","").toString()

        val imageBytes = Base64.getDecoder().decode(imageBs)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        imgProfileActivity.setImageBitmap(decodedImage)

        txtUserName.text = userName



        btn_changeImage.setOnClickListener {
            changeImage()
        }

        btnOnBack.setOnClickListener{
            showHome()
        }

        btnLogout.setOnClickListener {
            showStart()
        }

        button5.setOnClickListener {
            val  activityIntent =  Intent(this, OtherProfileActivity::class.java)
            activityIntent.putExtra("userId", idUser.toInt())
            startActivity(activityIntent)
            finish()
        }
    }

    private fun getAvatar() {
      //  val avatar:ByteArray? = dataDBHelper.getAvatar()
        /*if(avatar != null){
            imgProfileActivity.setImageBitmap(ImageUtilities.getBitMapFromByteArray(avatar))
        }*/

    }

    private fun changeImage() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            var boolDo:Boolean =  false
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, 1001)
            }
            else{
                //permission already granted
                boolDo =  true

            }


            if(boolDo == true){
                pickImageFromGallery()
            }

        }

    }

    private fun pickImageFromGallery() {
        //Abrir la galería
        val intent  =  Intent()
        intent.setAction(Intent.ACTION_PICK);
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    private fun showHome() {
        val activityHome = Intent(this, HomeActivity::class.java)
        startActivity(activityHome)
        finish()
    }

    private fun showStart() {
        val activityHome = Intent(this, MainActivity::class.java)

        sharedPreferences = getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("id","")
        editor.putString("userName","")
        editor.putString("email","")
        editor.putString("password","")
        editor.putString("imageUrl","")
        editor.apply()

        startActivity(activityHome)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            1001 -> {
                if (grantResults.size >0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //PERMISO DENEGADO
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, intent)

        if (resultcode == Activity.RESULT_OK) {

            if(requestcode == 1000) {

                this.imgProfileActivity.setImageURI(data?.data)

                val bitmap = (imgProfileActivity.getDrawable() as BitmapDrawable).bitmap

                val baos = ByteArrayOutputStream()

                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
               // dataDBHelper.insertAvatar(baos.toByteArray())
                imgArray = baos.toByteArray()
                saveImageUser();
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveImageUser() {
        val idUserString = idUser
        val encodedString:String =  Base64.getEncoder().encodeToString(this.imgArray)
        val user = User(idUserString.toInt(), null, null, null, encodedString)
        val userService : UserService = RestEngine.getRestEngine().create(UserService::class.java)
        val result: Call<String> = userService.updateImageUser(idUserString.toInt(), user)

        result.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                println("NICE")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println(t.toString())
            }

        })
    }

}