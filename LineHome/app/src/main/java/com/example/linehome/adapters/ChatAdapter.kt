package com.example.linehome.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.linehome.R
import com.example.linehome.models.Chat
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatAdapter(private val context: Context,private val chatList: List<Chat>):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View):RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView =  LayoutInflater.from(context).inflate(R.layout.item_chat,parent,false)
        return  ChatAdapter.ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = chatList[position]
        holder.itemView.txtUserChat.text = "el id del usuario es" + item.to
    }

    override fun getItemCount(): Int {
       return chatList.size

    }
}