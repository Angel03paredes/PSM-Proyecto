package com.example.linehome

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linehome.adapters.PostAdapter
import com.example.linehome.models.*
import com.example.linehome.services.*
import kotlinx.android.synthetic.main.activity_home.btnOnBack
import kotlinx.android.synthetic.main.activity_other_profile.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OtherProfileActivity : AppCompatActivity() {

    var userId: Int? = null

    private var postAdapter: PostAdapter? = null
    val listPost = mutableListOf<PostPreview>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)

        val bundle = intent.extras
        userId = bundle?.getInt("userId")!!

        postAdapter = PostAdapter(this, listPost )

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rvUserPost.setLayoutManager(llm)
        rvUserPost.setAdapter(postAdapter)

        btnOnBack.setOnClickListener{
            finish()
        }

        getUser()
    }

    private fun getUser() {
        var userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
        val result: Call<User> = userService.getUserById(userId!!.toInt())

        result.enqueue(object : Callback<User> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<User>, response: Response<User>) {
                var user = response.body()

                if(user != null) {
                    val imageBytes = Base64.getDecoder().decode(user.imageUrl)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    imgProfileActivity.setImageBitmap(decodedImage)

                    txtUserName.text = user.userName

                    getPublication()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println(t.toString())
            }

        })
    }

    private fun getPublication() {
        //DBApplication.dataDBHelper.truncatePublicatePreview()
        val publicationService: PublicationService = RestEngine.getRestEngine().create(
            PublicationService::class.java)
        val result: Call<List<Post>> = publicationService.getPublicationByOwner(userId!!.toInt())

        result.enqueue(object : Callback<List<Post>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                var posts = response.body()

                if(posts != null) {
                    for(post in posts) {

                        val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
                        val resultUser: Call<User> = userService.getUserById(post.owner!!)

                        val publicationPhotoService: PublicationPhotoService = RestEngine.getRestEngine().create(
                            PublicationPhotoService::class.java)
                        val resultImage: Call<PublicationPhoto> = publicationPhotoService.getFirstImageByPublication(post.id!!)

                        val publicationEvaluationService: PublicationEvaluationService = RestEngine.getRestEngine().create(
                            PublicationEvaluationService::class.java)
                        val resultEvaluation: Call<EvaluationPreview> = publicationEvaluationService.getEvaluationByPublication(post.id!!)

                        var owner: User? = null
                        var publicationImage: PublicationPhoto? = null
                        var evaluationPreview: EvaluationPreview? = null

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
                                    evaluationPreview = evaluation

                                    if(evaluationPreview?.average == null) evaluationPreview?.average = 0
                                }
                            }

                            override fun onFailure(call: Call<EvaluationPreview>, t: Throwable) {
                                println(t.toString())
                            }

                        })

                        resultImage.enqueue(object : Callback<PublicationPhoto> {
                            override fun onResponse(call: Call<PublicationPhoto>, response: Response<PublicationPhoto>) {
                                var image = response.body()
                                if(image != null) {
                                    publicationImage = PublicationPhoto(image.id, image.publicationId, image.image)

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

                                    if(evaluationPreview != null) {
                                        postPreview = PostPreview(post.id, owner?.userName, owner?.id, decodedImageOwner, decodedImagePublication, post.titlePublication, post.description, evaluationPreview?.average, post.location, post.price, post.createdAt)
                                    } else {
                                        postPreview = PostPreview(post.id, owner?.userName, owner?.id, decodedImageOwner, decodedImagePublication, post.titlePublication, post.description, 0, post.location, post.price, post.createdAt)
                                    }
                                    //DBApplication.dataDBHelper.insertPublicationPreview(postPreview)
                                    listPost.add(postPreview)
                                    postAdapter?.notifyDataSetChanged()
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
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                println(t.toString())
            }

        })
    }
}