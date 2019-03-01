package com.jointdelivery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class InfoWindowCustom(internal var context: Context) : GoogleMap.InfoWindowAdapter {
    internal lateinit var inflater: LayoutInflater

    override fun getInfoWindow(marker: Marker): View {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // R.layout.echo_info_window is a layout in my
        // res/layout folder. You can provide your own
        val v = inflater.inflate(R.layout.echo_info_window, null)

        val title = v.findViewById<View>(R.id.info_window_title) as TextView
        val subtitle = v.findViewById<View>(R.id.info_window_subtitle) as TextView
        title.text = marker.title
        subtitle.text = marker.snippet
        return v
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
}