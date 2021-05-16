package com.example.linehome

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linehome.adapters.PostAdapter
import com.example.linehome.models.*
import com.example.linehome.services.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.log

class HomeFragment : Fragment() {

    private var postAdapter:PostAdapter? = null
    val listPost = mutableListOf<PostPreview>()

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

        getPublication()

        return view
    }

    private fun getPublication() {
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
                                        postPreview = PostPreview(post.id, owner?.userName, decodedImageOwner, decodedImagePublication, post.titlePublication, post.description, evaluationPreview?.average, post.location, post.price, post.createdAt)
                                    } else {
                                        postPreview = PostPreview(post.id, owner?.userName, decodedImageOwner, decodedImagePublication, post.titlePublication, post.description, 0, post.location, post.price, post.createdAt)
                                    }

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

                        /*var decodedImageOwner: Bitmap? = null
                        var decodedImagePublication: Bitmap? = null

                        if(owner?.imageUrl != null) {
                            var imageBytes = Base64.getDecoder().decode(owner?.imageUrl)
                            decodedImageOwner = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        }

                        if(publicationImage?.image != null) {
                            val imageBytes = Base64.getDecoder().decode(publicationImage?.image)
                            decodedImagePublication = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        }*/

                        //val postPreview = PostPreview(post.id, owner?.userName, decodedImageOwner, decodedImagePublication, post.titlePublication, post.description, 4, post.location, post.price, post.createdAt)

                        //listPost.add(postPreview)
                    }

                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                println(t.toString())
            }

        })
    }
}