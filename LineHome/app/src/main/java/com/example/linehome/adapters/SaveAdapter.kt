package com.example.linehome.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linehome.OtherProfileActivity
import com.example.linehome.PostActivity
import com.example.linehome.R
import com.example.linehome.models.PostPreview
import kotlinx.android.synthetic.main.item_save.view.*

class SaveAdapter(private val context: Context, private val listPost: List<PostPreview>):RecyclerView.Adapter<SaveAdapter.SaveViewHolder>() {
    class SaveViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveAdapter.SaveViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_save,parent,false)
        return  SaveAdapter.SaveViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SaveAdapter.SaveViewHolder, position: Int) {
        val item = listPost[position]
        holder.itemView.userNamePost.text = item.ownerName
        holder.itemView.titlePost.text = item.titlePublication
        holder.itemView.datePost.text = item.createdAt
        holder.itemView.pricePost.text = item.price.toString()
        holder.itemView.locationPost.text = item.location
        if(item.imageOwner != null) {
            holder.itemView.imageView4.setImageBitmap(item.imageOwner)
        }

        if(item.imagePublication != null) {
            holder.itemView.imageView6.setImageBitmap(item.imagePublication)
        }

        holder.itemView.cvSave.setOnClickListener {
            val  activityIntent =  Intent(context, PostActivity::class.java)
            activityIntent.putExtra("publicationId", item.id)
            context?.startActivity(activityIntent)
        }

        holder.itemView.toolbar2.setOnClickListener {
            val  activityIntent =  Intent(context, OtherProfileActivity::class.java)
            activityIntent.putExtra("userId", item.ownerId)
            context?.startActivity(activityIntent)
        }
    }

    override fun getItemCount(): Int {
        return listPost.size
    }
}