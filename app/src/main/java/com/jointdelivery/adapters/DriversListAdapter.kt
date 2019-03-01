package com.jointdelivery.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jointdelivery.R
import com.jointdelivery.utilities.onRecyclerViewClickListener
import kotlinx.android.synthetic.main.row_assigned_item.view.*



class DriversListAdapter(var context: Context,var monItemClickListener: onRecyclerViewClickListener) :
    RecyclerView.Adapter<DriversListAdapter.ViewHolder>(),View.OnClickListener  {

    override fun onClick(v: View?) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_assigned_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.relative_item_layout.setOnClickListener{
            monItemClickListener.onListItemClicks(position)
        }
    }


    override fun getItemCount(): Int {
        return 30
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text_user_name: TextView = view.text_user_name
        val user_image: ImageView = view.user_image
        val text_time: TextView = view.text_time
        val text_status: TextView = view.text_status
        var relative_item_layout:CardView=view.cardView_item_layout





    }
}



