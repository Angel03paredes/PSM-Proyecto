package com.example.linehome.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linehome.R
import com.example.linehome.models.Post
import kotlinx.android.synthetic.main.item_home.view.*

class SaveAdapter(private val context: Context, private val listPost: List<Post>):RecyclerView.Adapter<SaveAdapter.SaveViewHolder>() {
    class SaveViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveAdapter.SaveViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_save,parent,false)
        return  SaveAdapter.SaveViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SaveAdapter.SaveViewHolder, position: Int) {
        val item = listPost[position]
        holder.itemView.userNamePost.text = "El id del usuario es: " + item.owner
        holder.itemView.titlePost.text = item.titlePublication
        holder.itemView.datePost.text = item.createdAt
        holder.itemView.pricePost.text = item.price.toString()
        holder.itemView.locationPost.text = item.location
    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}