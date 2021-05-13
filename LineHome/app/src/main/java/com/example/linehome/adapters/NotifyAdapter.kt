package com.example.linehome.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linehome.R
import com.example.linehome.models.Notify
import kotlinx.android.synthetic.main.item_notification.view.*

class NotifyAdapter(private val context: Context, private val notifyList:List<Notify>):RecyclerView.Adapter<NotifyAdapter.NotifyViewHoler>(){
    class NotifyViewHoler(view: View):RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHoler {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_notification,parent,false)
        return  NotifyAdapter.NotifyViewHoler(itemView)
    }

    override fun onBindViewHolder(holder: NotifyViewHoler, position: Int) {
       val item = notifyList[position]
        holder.itemView.txtMessageNotify.text = item.content
    }

    override fun getItemCount(): Int {
        return notifyList.size
    }
}