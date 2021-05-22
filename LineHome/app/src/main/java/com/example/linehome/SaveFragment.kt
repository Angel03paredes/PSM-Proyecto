package com.example.linehome

import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linehome.adapters.SaveAdapter
import com.example.linehome.models.*
import com.example.linehome.services.*
import kotlinx.android.synthetic.main.fragment_save.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SaveFragment : Fragment() {

    private var postAdapter:SaveAdapter? = null
    val listSave = mutableListOf<PostPreview>()
    var user: String? = null

    var INTERNET_AVAILABLE : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_save, container, false)

        val sharedPreferences : SharedPreferences = requireContext().getSharedPreferences("SharedP", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("id", "")


        //listSave.add(Post(1,"Casa de lujo","Esta casa evaluada el 300,000 pesos en Jalisco", 2300000, "Gudalajara,Jalisco","Casas",1))
        postAdapter = SaveAdapter(requireContext(), listSave )

        val llm = LinearLayoutManager(requireContext())
        llm.orientation = LinearLayoutManager.VERTICAL
        view.rvSave.setLayoutManager(llm)
        view.rvSave.setAdapter(postAdapter)

        INTERNET_AVAILABLE = isNetDisponible()

        if(INTERNET_AVAILABLE) {
            getPublication()
        }
        else {
            showPublicationsDisconected()
        }
        return view

        return view
    }

    private fun getPublication() {
        DBApplication.dataDBHelper.truncateSavePublication()
        val savePublicationService: SavePublicationService = RestEngine.getRestEngine().create(
            SavePublicationService::class.java)
        val result: Call<List<Post>> = savePublicationService.getSavePublicationsByUser(user!!.toInt())

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
                                    DBApplication.dataDBHelper.insertSavePublication(postPreview)
                                    listSave.add(postPreview)
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

    private fun showPublicationsDisconected() {
        var listPublication:List<PostPreview>
        listPublication = DBApplication.dataDBHelper.getSavePublication(user!!.toInt())
        for(post in listPublication){
            listSave.add(post)
            postAdapter?.notifyDataSetChanged()
        }
    }

    private fun isNetDisponible(): Boolean {

        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetInfo = connectivityManager.activeNetworkInfo
        return actNetInfo != null && actNetInfo.isConnected
    }

}