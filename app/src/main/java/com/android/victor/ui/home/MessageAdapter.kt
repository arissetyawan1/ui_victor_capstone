package com.android.victor.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.victor.R
import com.android.victor.model.MessageModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class MessageAdapter(options: FirebaseRecyclerOptions<MessageModel>) :
    FirebaseRecyclerAdapter<MessageModel, MessageAdapter.MessageViewHolder>(options) {

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int, model: MessageModel
    ) {
        holder.time.text = "${model.messageTime}"
        holder.message.text = model.messageText
        holder.name.text = model.messageUser
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var time: TextView = itemView.findViewById(R.id.tvTime)
        var message: TextView = itemView.findViewById(R.id.tvMessage)
        var name: TextView = itemView.findViewById(R.id.tvMessageName)
    }
}