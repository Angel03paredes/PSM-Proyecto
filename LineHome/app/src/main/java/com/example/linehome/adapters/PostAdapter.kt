package com.example.linehome.adapters

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.linehome.OtherProfileActivity
import com.example.linehome.PostActivity
import com.example.linehome.R
import com.example.linehome.models.Post
import com.example.linehome.models.PostPreview
import kotlinx.android.synthetic.main.item_home.view.*

class PostAdapter(private val context: Context, private val listPost: List<PostPreview>):RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    var INTERNET_AVAILABLE : Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_home,parent,false)
        return  PostAdapter.PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
        val item = listPost[position]
        holder.itemView.userNamePost.text = item.ownerName
        holder.itemView.titlePost.text = item.titlePublication
        holder.itemView.descriptionPost.text = item.description
        holder.itemView.datePost.text = item.createdAt
        holder.itemView.pricePost.text = item.price.toString()
        holder.itemView.locationPost.text = item.location
        holder.itemView.ratingBar.rating = item.evaluation!!.toFloat()
        if(item.imageOwner != null) {
            holder.itemView.imageView4.setImageBitmap(item.imageOwner)
        }

        if(item.imagePublication != null) {
            holder.itemView.imageView6.setImageBitmap(item.imagePublication)
        }

        holder.itemView.cvPost.setOnClickListener {
            val  activityIntent =  Intent(context, PostActivity::class.java)
            activityIntent.putExtra("publicationId", item.id)
            context?.startActivity(activityIntent)
        }

        holder.itemView.toolbar2.setOnClickListener {
            INTERNET_AVAILABLE = isNetDisponible()

            if(INTERNET_AVAILABLE) {
                if(item.ownerId != null) {
                    val  activityIntent =  Intent(context, OtherProfileActivity::class.java)
                    activityIntent.putExtra("userId", item.ownerId!!)
                    context?.startActivity(activityIntent)
                } else {
                    Toast.makeText(context,"En este momento no se encuentra disponible esta acción",Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context,"En este momento no se encuentra disponible esta función",Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun getItemCount(): Int {
        return listPost.size
    }

    private fun isNetDisponible(): Boolean {
        val connectivityManager = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val actNetInfo = connectivityManager.activeNetworkInfo
        return actNetInfo != null && actNetInfo.isConnected
    }
}