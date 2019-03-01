package com.jointdelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.jointdelivery.R

open class NotificationAdapter(var context: Context) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        return NotificationAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_notifications_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return 30
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


}