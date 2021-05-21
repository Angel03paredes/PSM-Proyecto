package com.example.linehome.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.linehome.R
import com.example.linehome.models.NotificationPreview
import com.example.linehome.models.Notify
import com.example.linehome.models.NotifyPreview
import kotlinx.android.synthetic.main.item_notification.view.*
import java.util.*

class NotifyAdapter(private val context: Context, private val notifyList:List<NotifyPreview>):RecyclerView.Adapter<NotifyAdapter.NotifyViewHoler>(){
    class NotifyViewHoler(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHoler {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_notification,parent,false)
        return  NotifyAdapter.NotifyViewHoler(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NotifyViewHoler, position: Int) {
       val item = notifyList[position]
        holder.itemView.txtMessageNotify.text = "Te ha calificado"

        holder.itemView.imageView5.setImageBitmap(item.imageUrl)

        holder.itemView.txtUserChat.text = item.userName
        holder.itemView.txtDateChat.text = item.createdAt
    }

    override fun getItemCount(): Int {
        return notifyList.size
    }
}