package com.example.linehome

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.linehome.models.*
import com.example.linehome.services.*
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PostActivity : AppCompatActivity() {

    var sampleImages = intArrayOf(
        R.drawable.dubai,
        R.drawable.moscow,
        R.drawable.paris,
        R.drawable.uk
    )

    var publicationId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val bundle = intent.extras
        publicationId = bundle?.getInt("publicationId")!!

        btnOnBack.setOnClickListener {
            finish()
        }

        carouselView.setPageCount(sampleImages.size);
        carouselView.setImageListener(imageListener)

        getPublication()
    }

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView) {
            // You can use Glide or Picasso here
            imageView.setImageResource(sampleImages[position])
        }
    }

    private fun getPublication() {
        val publicationService: PublicationService = RestEngine.getRestEngine().create(PublicationService::class.java)
        val result: Call<Post> = publicationService.getPublicationById(publicationId)

        result.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                var resPost = response.body()

                if(resPost != null) {
                    var post = Post(resPost.id, resPost.titlePublication, resPost.description, resPost.price, resPost.location, resPost.category, resPost.owner, resPost.createdAt)

                    val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
                    val resultUser: Call<User> = userService.getUserById(post.owner!!)

                    val publicationPhotoService: PublicationPhotoService = RestEngine.getRestEngine().create(
                        PublicationPhotoService::class.java)
                    val resultImage: Call<PublicationPhoto> = publicationPhotoService.getFirstImageByPublication(post.id!!)

                    val publicationEvaluationService: PublicationEvaluationService = RestEngine.getRestEngine().create(
                        PublicationEvaluationService::class.java)
                    val resultEvaluation: Call<EvaluationPreview> = publicationEvaluationService.getEvaluationByPublication(post.id!!)

                    var owner: User? = null
                    var publicationImage: List<PublicationPhoto>? = null
                    var evaluationP: EvaluationPreview? = null

                    resultUser.enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            val user = response.body()
                            if(user != null){
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

                            if(evaluation != null) {
                                evaluationP = evaluation

                                if(evaluationP?.average == null) evaluationP?.average = 0
                            }
                        }

                        override fun onFailure(call: Call<EvaluationPreview>, t: Throwable) {
                            println(t.toString())
                        }
                    })

                    resultImage.enqueue(object : Callback<PublicationPhoto> {
                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun onResponse(call: Call<PublicationPhoto>, response: Response<PublicationPhoto>) {
                            var image = response.body()
                            if(image != null) {
                                /*publicationImage = PublicationPhoto(image.id, image.publicationId, image.image)

                                var decodedImageOwner: Bitmap? = null
                                var decodedImagePublication: Bitmap? = null

                                if(owner?.imageUrl != null) {
                                    var imageBytes = Base64.getDecoder().decode(owner?.imageUrl)
                                    decodedImageOwner = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                }

                                if(publicationImage?.image != null) {
                                    val imageBytes = Base64.getDecoder().decode(publicationImage?.image)
                                    decodedImagePublication = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                }

                                var postPreview: PostPreview? = null

                                if(evaluationP != null) {
                                    postPreview = PostPreview(post.id, owner?.userName, decodedImageOwner, decodedImagePublication, post.titlePublication, post.description, evaluationPreview?.average, post.location, post.price, post.createdAt)
                                } else {
                                    postPreview = PostPreview(post.id, owner?.userName, decodedImageOwner, decodedImagePublication, post.titlePublication, post.description, 0, post.location, post.price, post.createdAt)
                                }*/

                                var decodedImageOwner: Bitmap? = null

                                if(owner?.imageUrl != null) {
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

                        override fun onFailure(call: Call<PublicationPhoto>, t: Throwable) {
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
}