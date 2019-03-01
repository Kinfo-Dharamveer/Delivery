package com.jointdelivery.appviewmodule.mapactivities

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.jointdelivery.CustomInfoWindowGoogleMap
import com.jointdelivery.InfoWindowData
import com.jointdelivery.R
import kotlinx.android.synthetic.main.bottom_order_detail.*
import kotlinx.android.synthetic.main.bottom_your_rout_activity.*
import kotlinx.android.synthetic.main.dialog_confirmation.*
import kotlinx.android.synthetic.main.tool_bar_layout.*

open class OrderDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap?) {
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

        var snowqualmie = LatLng(47.5287132, -121.8253906);

        var markerOptions = MarkerOptions();
        markerOptions.position(snowqualmie)
            .title("Snowqualmie Falls")
            .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        var info = InfoWindowData();
        info.setImage("snowqualmie");
        info.setHotel("Hotel : excellent hotels available");
        info.setFood("Food : all types of restaurants available");
        info.setTransport("Reach the site by bus, car and train.");

        var customInfoWindow = CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);


        mMap.addMarker(markerOptions);
        var m: Marker = mMap.addMarker(markerOptions);
        m.setTag(info);
        m.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(snowqualmie));

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_layout)
        toolbar_title.text = resources.getString(R.string.order_detail)
        back_button.visibility = View.VISIBLE


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        back_button.setOnClickListener {
            onBackPressed()
        }
    }

    fun rideClick(view: View) {
        openCustomDialog()
    }



    override fun onBackPressed() {
        super.onBackPressed()
    }
    fun showDetail(view: View) {
        if (detail_text_view.visibility != View.VISIBLE) {
            show_details.text = "Hide Details"
            detail_text_view.visibility = View.VISIBLE
        } else {
            show_details.text = "Show Details"
            detail_text_view.visibility = View.GONE
        }
    }




    fun openCustomDialog() {
        val dialog: androidx.appcompat.app.AlertDialog

        val inflater = layoutInflater
        val alertLayout = inflater.inflate(R.layout.dialog_confirmation, null)

        val alert = androidx.appcompat.app.AlertDialog.Builder(this)
        alert.setView(alertLayout)

        dialog = alert.create()
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()

        dialog.yes_btn.setOnClickListener {
            val intent = Intent(this, YourRideMapActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.cencel_btn.setOnClickListener {
            dialog.dismiss()
        }
    }
}