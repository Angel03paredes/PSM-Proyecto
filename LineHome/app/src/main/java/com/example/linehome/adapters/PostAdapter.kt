package com.example.linehome.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linehome.PostActivity
import com.example.linehome.R
import com.example.linehome.models.Post
import com.example.linehome.models.PostPreview
import kotlinx.android.synthetic.main.item_home.view.*

class PostAdapter(private val context: Context, private val listPost: List<PostPreview>):RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.PostViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_home,parent,false)
        return  PostAdapter.PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostAdapter.PostViewHolder, position: Int) {
        val item = listPost[position]
        holder.itemView.userNamePost.text = item.ownerName
        holder.itemView.titlePost.text = item.titlePublication
        holder.itemView.descriptionPost.setText(item.description)
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

    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}