package com.jointdelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.jointdelivery.R

open class MessagesAdapter(var context: Context) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesAdapter.ViewHolder {
        return MessagesAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_messages_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 30
    }

    override fun onBindViewHolder(holder: MessagesAdapter.ViewHolder, position: Int) {
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }


}