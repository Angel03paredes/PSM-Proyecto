package com.example.linehome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linehome.DBApplication.Companion.dataDBHelper
import com.example.linehome.adapters.PostAdapter
import com.example.linehome.models.*
import com.example.linehome.services.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.util.*

import kotlin.math.log

class HomeFragment : Fragment() {

    private var postAdapter:PostAdapter? = null
    val listPost = mutableListOf<PostPreview>()
    var INTERNET_AVAILABLE : Boolean = true

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPreferences : SharedPreferences = requireContext().getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        val user = sharedPreferences.getString("id", "")

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        postAdapter = PostAdapter(requireContext(), listPost )

        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        view.rvPostHome.setLayoutManager(llm)
        view.rvPostHome.setAdapter(postAdapter)

        INTERNET_AVAILABLE = isNetDisponible()

        if(INTERNET_AVAILABLE) {
            getPublication()
        }
        else {
            showPublicationsDisconected()
        }

        view.swipeRefresh.setColorSchemeResources(R.color.primary_color);
        view.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.white);
        view.swipeRefresh.setOnRefreshListener {
            INTERNET_AVAILABLE = isNetDisponible()
            if(INTERNET_AVAILABLE) {
                postAdapter?.notifyDataSetChanged()
            }
            else {
                postAdapter?.notifyDataSetChanged()
            }
            view.swipeRefresh.isRefreshing = false
        }

        return view
    }

    private fun showPublicationsDisconected() {
        var listPublication:List<PostPreview>
        listPublication = dataDBHelper.getPublicationPreview()
        for(post in listPublication){
            listPost.add(post)
            postAdapter?.notifyDataSetChanged()
        }
    }

    private fun getPublication() {
        dataDBHelper.truncatePublicatePreview()
        dataDBHelper.truncatePublicationPhotoPreview()
        val publicationService: PublicationService = RestEngine.getRestEngine().create(PublicationService::class.java)
        val result: Call<List<Post>> = publicationService.getPublications()

        result.enqueue(object : Callback<List<Post>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                var posts = response.body()

                if(posts != null) {
                    for(post in posts) {

                        val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
                        val resultUser: Call<User> = userService.getUserById(post.owner!!)

                        val publicationPhotoService: PublicationPhotoService = RestEngine.getRestEngine().create(PublicationPhotoService::class.java)
                        val resultImage: Call<PublicationPhoto> = publicationPhotoService.getFirstImageByPublication(post.id!!)

                        val publicationEvaluationService: PublicationEvaluationService = RestEngine.getRestEngine().create(PublicationEvaluationService::class.java)
                        val resultEvaluation: Call<EvaluationPreview> = publicationEvaluationService.getEvaluationByPublication(post.id!!)

                        var owner: User? = null
                        var publicationImage: PublicationPhoto? = null
                        var evaluationPreview: EvaluationPreview? = null

                        resultUser.enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                val user = response.body()
                                if(user != null){
                                    owner = User(user.id, user.userName, user.email, null, user.imageUrl)
                                    /////////////////Retrofit Enqueue Para Evaluation
                                    resultEvaluation.enqueue(object : Callback<EvaluationPreview> {
                                        override fun onResponse(call: Call<EvaluationPreview>, response: Response<EvaluationPreview>) {
                                            val evaluation = response.body()

                                            if(evaluation != null) {
                                                evaluationPreview = evaluation

                                                if(evaluationPreview?.average == null) evaluationPreview?.average = 0
                                                //////////Retrofit enqueue Para Imagen


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
                                                            dataDBHelper.insertPublicationPreview(postPreview)
                                                            val idLastPost = dataDBHelper.getLastIdPublicationPreview()
                                                            ///////////Para traer las tres imagenes y guardarlas en sqlite

                                                            val publicationPhotoService: PublicationPhotoService = RestEngine.getRestEngine().create(
                                                                    PublicationPhotoService::class.java)
                                                            val resultImagesfromPost: Call<List<PublicationPhoto>> = publicationPhotoService.getPublicationPhotoByPublicationId(post.id!!)
                                                            resultImagesfromPost.enqueue(object : Callback<List<PublicationPhoto>> {
                                                                @RequiresApi(Build.VERSION_CODES.O)
                                                                override fun onResponse(call: Call<List<PublicationPhoto>>, response: Response<List<PublicationPhoto>>) {
                                                                    var images = response.body()
                                                                    if (images != null) {
                                                                        for (image in images) {
                                                                            var imageBytes = Base64.getDecoder().decode(image.image)
                                                                            var decodeImg = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                                                            dataDBHelper.insertPublicationPreviewPhoto(idLastPost!!,decodeImg)
                                                                        }
                                                                    }
                                                                }

                                                                override fun onFailure(call: Call<List<PublicationPhoto>>, t: Throwable) {
                                                                    print(t.toString())
                                                                }

                                                            })



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

                                        override fun onFailure(call: Call<EvaluationPreview>, t: Throwable) {
                                            println(t.toString())
                                        }

                                    })

                                } else {
                                    println("Usuario no encontrado.")
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
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


    private fun isNetDisponible(): Boolean {

        val connectivityManager = requireContext().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetInfo = connectivityManager.activeNetworkInfo
        return actNetInfo != null && actNetInfo.isConnected
    }

}