package com.jointdelivery.appviewmodule.mapactivities

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.jointdelivery.R
import kotlinx.android.synthetic.main.tool_bar_layout.*

class CompletedOrderDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap!!
        mMap?.getUiSettings()?.setMapToolbarEnabled(false);
        try {

            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style
                )
            )

            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("TAG", "Can't find style. Error: ", e)
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.completed_order_detail_activity)
        toolbar_title.setText("Order Detail")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        back_button.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}