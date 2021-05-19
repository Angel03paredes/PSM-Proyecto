package com.example.linehome.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.linehome.R
import kotlinx.android.synthetic.main.item_images.view.*


class ImagesLoadAdapter(private val context: Context, private val imageList: List<ByteArray>):RecyclerView.Adapter<ImagesLoadAdapter.ImagesViewHolder>() {
    class ImagesViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_images, parent, false)
        return  ImagesLoadAdapter.ImagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.itemView.txtImgInfo.text = "Recurso " + (position + 1) + " cargado."
        val bmp = BitmapFactory.decodeByteArray(imageList[position], 0, imageList[position].size)
        val image: ImageView = holder.itemView.imagePreview as ImageView
        image.setImageBitmap(Bitmap.createScaledBitmap(bmp, 50, 50, false))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


}