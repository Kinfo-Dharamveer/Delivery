package com.jointdelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jointdelivery.R
import com.jointdelivery.utilities.onRecyclerViewClickListener
import kotlinx.android.synthetic.main.row_completed_order_layout.view.*


open class CompletedOrderAdapter(var context: Context, var onRecyclerViewClickListener: onRecyclerViewClickListener) :
    RecyclerView.Adapter<CompletedOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedOrderAdapter.ViewHolder {
        return CompletedOrderAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_completed_order_layout,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return 30
    }


    override fun onBindViewHolder(holder: CompletedOrderAdapter.ViewHolder, position: Int) {
        holder.cardView_item_layout.setOnClickListener {
            onRecyclerViewClickListener.onListItemClicks(position)
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView_item_layout: CardView = view.cardView_item_layout
    }


}