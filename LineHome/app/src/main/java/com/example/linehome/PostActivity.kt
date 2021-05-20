package com.example.linehome

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.linehome.models.*
import com.example.linehome.services.*
import com.synnapps.carouselview.ViewListener
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.alert_ratingbar.*
import kotlinx.android.synthetic.main.item_carousel.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

class PostActivity : AppCompatActivity() {


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var idUser : String
    lateinit var dialog:Dialog
    var INTERNET_AVAILABLE : Boolean = true
    var showDialog: Boolean = false
    var listImages = mutableListOf<ByteArray>()
    var ratinCalificated: Int? = null

    var publicationId: Int = 0

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        dialog = Dialog(this)

        val bundle = intent.extras
        publicationId = bundle?.getInt("publicationId")!!

        btnOnBack.setOnClickListener {
            finish()
        }

        ratingBar.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (showDialog) {
                    showAlertRatingBar()
                } else {
                    Toast.makeText(this@PostActivity, "Esta publicación la calificaste con :" + ratinCalificated.toString(), Toast.LENGTH_LONG).show()
                }
            }
            return@OnTouchListener true
        })


        getPublication()

        checkEvaluation()




      showCarousel()

        INTERNET_AVAILABLE=isNetDisponible()
        if(!INTERNET_AVAILABLE){
            var publication:PostPreview
            publication = DBApplication.dataDBHelper.getPublicationPreviewById(publicationId)
       showPublicationDisconnected(publication)

        }
    }

    private fun checkEvaluation() {
        val sharedPreferences : SharedPreferences = getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("id", "").toString()

        val publicationEvaluationService: PublicationEvaluationService = RestEngine.getRestEngine().create(PublicationEvaluationService::class.java)
        val result: Call<Evaluation> = publicationEvaluationService.getEvaluationByUserAndPublication(publicationId, Integer.parseInt(idUser))

        result.enqueue(object : Callback<Evaluation> {
            override fun onResponse(call: Call<Evaluation>, response: Response<Evaluation>) {
                var resp = response.body()
                if (resp != null) {
                    ratinCalificated = resp.evaluation
                } else {
                    showDialog = true
                }
            }

            override fun onFailure(call: Call<Evaluation>, t: Throwable) {
                print(t)
            }
        })
    }

    private fun showAlertRatingBar() {

        dialog.setContentView(R.layout.alert_ratingbar)
        dialog.show()

        dialog.btnCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        //dialog.ratingBar2.setOnRatingBarChangeListener { ratingBar, fl, b ->  }

        dialog.button.setOnClickListener {
            val ratFloat:Float = dialog.ratingBar2.getRating()
            val rating:Int =  ratFloat.toInt()
            showDialog=false
            addEvaluation(rating)

        }
        
    }

    private fun addEvaluation(rating: Int) {
        val sharedPreferences : SharedPreferences = getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        idUser = sharedPreferences.getString("id", "").toString()
        val publicationEvaluationService: PublicationEvaluationService = RestEngine.getRestEngine().create(PublicationEvaluationService::class.java)
        val result: Call<Evaluation> = publicationEvaluationService.addEvaluation(Evaluation(null, publicationId, Integer.parseInt(idUser), rating))

        result.enqueue(object : Callback<Evaluation> {
            override fun onResponse(call: Call<Evaluation>, response: Response<Evaluation>) {
                var resp = response.body()
                if (resp != null) {
                    Toast.makeText(this@PostActivity, "Esta publicación la calificaste con : " + rating.toString(), Toast.LENGTH_LONG).show()
                    ratinCalificated = resp.evaluation
                    dialog.dismiss()

                }
            }

            override fun onFailure(call: Call<Evaluation>, t: Throwable) {
                print(t)
            }
        })
    }

    private fun showPublicationDisconnected(postPreview: PostPreview) {
        textViewTitlePost.text = postPreview.titlePublication
        textViewDateP.text = "Publicado en: " + postPreview.createdAt
        textViewDescription.text = postPreview.description
        pricePost.text = postPreview.price.toString()
        locationPost.text = postPreview.location
        ratingBar.rating = postPreview.evaluation?.toFloat()!!
        textViewUserPost.text = postPreview?.ownerName
        imageView8.setImageBitmap(postPreview.imageOwner)
        val baos = ByteArrayOutputStream()
        postPreview.imagePublication?.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        listImages.add(baos.toByteArray())
        showCarousel()
    }

    var imageListener: ViewListener = object : ViewListener  {
        override fun setViewForPosition(position: Int): View {
            val view : View = getLayoutInflater().inflate(R.layout.item_carousel, null);
            var decodeImg = BitmapFactory.decodeByteArray(listImages?.get(position), 0, listImages?.get(position)!!.size)
            view.imgPostCarousel.setImageBitmap(decodeImg)
            return view
        }
        // You can use Glide or Picasso here
            //imageView.setImageResource(sampleImages[position])

    }

    private fun getPublication() {
        val publicationService: PublicationService = RestEngine.getRestEngine().create(PublicationService::class.java)
        val result: Call<Post> = publicationService.getPublicationById(publicationId)

        result.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                var resPost = response.body()

                if (resPost != null) {
                    var post = Post(resPost.id, resPost.titlePublication, resPost.description, resPost.price, resPost.location, resPost.category, resPost.owner, resPost.createdAt)

                    val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
                    val resultUser: Call<User> = userService.getUserById(post.owner!!)

                    val publicationPhotoService: PublicationPhotoService = RestEngine.getRestEngine().create(
                            PublicationPhotoService::class.java)
                    val resultImage: Call<List<PublicationPhoto>> = publicationPhotoService.getPublicationPhotoByPublicationId(post.id!!)

                    val publicationEvaluationService: PublicationEvaluationService = RestEngine.getRestEngine().create(
                            PublicationEvaluationService::class.java)
                    val resultEvaluation: Call<EvaluationPreview> = publicationEvaluationService.getEvaluationByPublication(post.id!!)

                    var owner: User? = null
                    var publicationImage: List<PublicationPhoto>? = null
                    var evaluationP: EvaluationPreview? = null

                    resultUser.enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            val user = response.body()
                            if (user != null) {
                                owner = User(user.id, user.userName, user.email, null, user.imageUrl)
                            } else {
                                println("Usuario no encontrado.")
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            println(t.toString())
                        }
                    })

                    resultEvaluation.enqueue(object : Callback<EvaluationPreview> {
                        override fun onResponse(call: Call<EvaluationPreview>, response: Response<EvaluationPreview>) {
                            val evaluation = response.body()

                            if (evaluation != null) {
                                evaluationP = evaluation

                                if (evaluationP?.average == null) evaluationP?.average = 0
                            }
                        }

                        override fun onFailure(call: Call<EvaluationPreview>, t: Throwable) {
                            println(t.toString())
                        }
                    })

                    resultImage.enqueue(object : Callback<List<PublicationPhoto>> {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onResponse(call: Call<List<PublicationPhoto>>, response: Response<List<PublicationPhoto>>) {
                            var images = response.body()
                            if (images != null) {
                                for (image in images) {
                                    var imageBytes = Base64.getDecoder().decode(image.image)
                                    //  var decodeImg = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                    listImages.add(imageBytes)
                                }

                                showCarousel()


                                var decodedImageOwner: Bitmap? = null

                                if (owner?.imageUrl != null) {
                                    var imageBytes = Base64.getDecoder().decode(owner?.imageUrl)
                                    decodedImageOwner = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                }

                                textViewTitlePost.text = post.titlePublication
                                textViewDateP.text = "Publicado en: " + post.createdAt
                                textViewDescription.text = post.description
                                pricePost.text = post.price.toString()
                                locationPost.text = post.location
                                ratingBar.rating = evaluationP?.average!!.toFloat()
                                textViewUserPost.text = owner?.userName
                                imageView8.setImageBitmap(decodedImageOwner)


                            } else {
                                println("Imagen no encontrada.")
                            }
                        }

                        override fun onFailure(call: Call<List<PublicationPhoto>>, t: Throwable) {
                            println(t.toString())
                        }
                    })
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                println(t.toString())
            }

        })


    }

    private fun showCarousel() {
        carouselView.pageCount = listImages.size
        carouselView.setViewListener(imageListener)
    }

    private fun isNetDisponible(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetInfo = connectivityManager.activeNetworkInfo
        return actNetInfo != null && actNetInfo.isConnected
    }

}