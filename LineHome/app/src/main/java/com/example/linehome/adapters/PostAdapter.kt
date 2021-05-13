package com.example.linehome.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linehome.R
import com.example.linehome.models.Post
import kotlinx.android.synthetic.main.item_home.view.*

class PostAdapter(private val context: Context, private val listPost: List<Post>):RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_home,parent,false)
        return  PostAdapter.PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
        val item = listPost[position]
        holder.itemView.userNamePost.text = "El id del usuario es: " + item.user
        holder.itemView.titlePost.text = item.title
        holder.itemView.descriptionPost.setText(item.description)
        holder.itemView.datePost.text = item.date.toString()
        holder.itemView.pricePost.text = item.price
        holder.itemView.locationPost.text = item.location
    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}