    package com.example.linehome

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.linehome.DBApplication.Companion.dataDBHelper
import com.example.linehome.adapters.ImagesLoadAdapter
import com.example.linehome.models.Post
import com.example.linehome.models.PublicationPhoto
import com.example.linehome.services.PublicationPhotoService
import com.example.linehome.services.PublicationService
import com.example.linehome.services.RestEngine
import com.example.linehome.services.UserService
import kotlinx.android.synthetic.main.activity_c_m_s.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.btnOnBack
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*


    class CMSActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    val categories = arrayOf("Estudio", "Piso", "Dúplex", "Ático", "Bajo", "Loft", "Departamento")
    var category: String = ""
    val imageList = mutableListOf<ByteArray>()
    var INTERNET_AVAILABLE : Boolean = true
    private lateinit var idUser:String


    private lateinit var imagesLoadAdapter: ImagesLoadAdapter
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_m_s)

        val sharedPreferences : SharedPreferences = getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("id","").toString()

        btnOnBack.setOnClickListener{
            showHome()
        }

        btnCreatePost.setOnClickListener {
            savePublication()
        }

        btn_camera.setOnClickListener{
            changeImage(2)
        }
        btn_gallery.setOnClickListener {
            changeImage(1)
        }

        imagesLoadAdapter = ImagesLoadAdapter(this, imageList)
        rv_images.adapter = imagesLoadAdapter

        spinner3.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories)

        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                category = categories.get(0)
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                category = categories.get(p2)
            }

        }

    }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun savePublication() {
            val title = editTextTextPersonName.text
            val description = editTextTextMultiLine2.text
            val price = editTextNumber.text
            val ubication = editUbication.text

            if(!category.isEmpty() && !title.isEmpty() && !description.isEmpty() && !price.isEmpty() && !ubication.isEmpty() && imageList.size >= 3){

                INTERNET_AVAILABLE = isNetDisponible()
                if(INTERNET_AVAILABLE){
                    var idPublication :  Int = 0
                   val post =   Post(null,title.toString(),description.toString(),Integer.parseInt(price.toString()),ubication.toString(),category,Integer.parseInt(idUser.toString()),null )
                    val publicationService : PublicationService = RestEngine.getRestEngine().create(PublicationService::class.java)
                    val result: Call<Post> = publicationService.addPublication(post)

                    result.enqueue(object : Callback<Post> {
                        override fun onResponse(call: Call<Post>, response: Response<Post>) {
                            val item = response.body()

                            if (item != null) {
                                idPublication = item.id!!
                                for (image in imageList){
                                    if (idPublication != null) {
                                        savePublicationPhotoApi(idPublication,image)
                                    }
                                }
                                clearCMS()
                                Toast.makeText(this@CMSActivity,"Tu publicación ha sido creada correctamente..",Toast.LENGTH_LONG).show()
                            }

                        }

                        override fun onFailure(call: Call<Post>, t: Throwable) {
                            print("Error: " + t)
                        }

                    })




                }else{
                    dataDBHelper.insertPublication(Post(null,title.toString(),description.toString(),Integer.parseInt(price.toString()),ubication.toString(),category,Integer.parseInt(idUser.toString()),java.util.Calendar.getInstance().toString() ),0)
                    val idPublication : Int? = dataDBHelper.getLastIdPublication()
                    for (image in imageList){
                        if (idPublication != null) {
                            dataDBHelper.insertPublicationPhoto(idPublication,image)
                        }
                    }
                    clearCMS()
                    Toast.makeText(this,"Tu publicación se subirá cuando estés conectado a la red.",Toast.LENGTH_LONG).show()
                }


            }else{
                Toast.makeText(this, "Todos los campos son requeridos y deben ser minimo 3 imagenes", Toast.LENGTH_LONG).show()
            }
        }

        private fun clearCMS() {
            editTextTextPersonName.text.clear()
            editTextTextMultiLine2.text.clear()
            editTextNumber.text.clear()
            editUbication.text.clear()
            imageList.clear()
            imagesLoadAdapter?.notifyDataSetChanged()
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



        private fun showHome() {
        val activityHome = Intent(this, HomeActivity::class.java)
        startActivity(activityHome)
        finish()
    }

        private fun changeImage(case: Int) {
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
                if(boolDo == true && case == 1){ //If gallery is selected
                    pickImageFromGallery()
                }
                if(boolDo == true && case == 2){ // if camera is selected
                    pickImageFromCamera()
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

        private fun pickImageFromCamera() {
            //Abrir la galería
            var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 1002)
        }


        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            when(requestCode){
                1001 -> {
                    if (grantResults.size > 0 && grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED) {
                        //permission from popup granted
                        pickImageFromGallery()
                    } else {
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

                        if (requestcode == 1002) {

                            val photo =  data?.extras?.get("data") as Bitmap
                            val stream = ByteArrayOutputStream()
                           photo.compress(Bitmap.CompressFormat.JPEG, 25, stream)
                            imageList.add(stream.toByteArray())
                            imagesLoadAdapter?.notifyDataSetChanged()



                        }

                if(requestcode == 1000) {

                    this.imageNoVisible.setImageURI(data?.data)

                    val bitmap = (imageNoVisible.getDrawable() as BitmapDrawable).bitmap

                  val baos = ByteArrayOutputStream()

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
                    imageList.add(baos.toByteArray())
                    imagesLoadAdapter?.notifyDataSetChanged()
                }
            }
        }

        private fun isNetDisponible(): Boolean {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val actNetInfo = connectivityManager.activeNetworkInfo
            return actNetInfo != null && actNetInfo.isConnected
        }

    }