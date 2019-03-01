package com.jointdelivery.appviewmodule.mapactivities

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jointdelivery.R
import com.jointdelivery.basemodule.models.MarkerData
import com.google.android.gms.maps.model.Marker
import android.os.Handler

import android.location.LocationManager
import android.provider.Settings

import android.location.Location
import android.util.Log

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import kotlinx.android.synthetic.main.tool_bar_layout.*
import com.google.android.gms.maps.model.MapStyleOptions
import com.jointdelivery.CustomInfoWindowGoogleMap
import com.jointdelivery.InfoWindowData


class YourRoutsMapActivity : AppCompatActivity(), GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, LocationListener,


    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    override fun onInfoWindowClick(marker: Marker?) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onLocationChanged(location: Location?) {
    }


    override fun onMarkerClick(p0: Marker?): Boolean {
        Handler().postDelayed(Runnable {
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    p0!!.getPosition(),
                    15.2f
                )
            )
        }, 300)
        return true
    }

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.your_routes_activity)

        toolbar_title.text = resources.getString(R.string.your_routes)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.getUiSettings()?.setMapToolbarEnabled(false);
        mMap.setOnInfoWindowClickListener(this);
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
        mMap.getUiSettings().setZoomControlsEnabled(true);



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
        addmarkerToMapViiew()
    }

    fun rideClick(view: View) {
        val intent = Intent(applicationContext, YourRideMapActivity::class.java)
        startActivity(intent)
    }

    fun addmarkerToMapViiew() {

        val markersArray = ArrayList<MarkerData>()
        val markerData = MarkerData()
        markerData.latitue = 30.7350626
        markerData.longitude = 76.6934885
        markerData.title = "Chandigarh"
        markersArray.add(markerData)

        val markerData2 = MarkerData()
        markerData2.latitue = 30.6938186
        markerData2.longitude = 76.6628846
        markerData2.title = "landra"
        markersArray.add(markerData2)

        val markerData3 = MarkerData()
        markerData3.latitue = 30.0495555
        markerData3.longitude = 60.3314421
        markerData3.title = "Pakistan"
        markersArray.add(markerData3)

        val markker4 = MarkerData()
        markker4.latitue = 30.6964447
        markker4.longitude = 76.6831728
        markker4.title = "Sohana Hospital"


        val markker5 = MarkerData()
        markker5.latitue = 30.6964447
        markker5.longitude = 76.6831728
        markker5.title = "Ivy Hospital"
        markersArray.add(markker5)


        val markker6 = MarkerData()
        markker6.latitue = 30.6981585
        markker6.longitude = 76.6886497
        markker6.title = "Rose Garden"
        markersArray.add(markker6)

        val markker7 = MarkerData()
        markker7.latitue = 30.6981585
        markker7.longitude = 76.6886497
        markker7.title = "Judicial Courts Complex"
        markersArray.add(markker7)
        val markker8 = MarkerData()
        markker8.latitue = 30.6994103
        markker8.longitude = 76.6601885
        markker8.title = "Dhaba"
        markersArray.add(markker8)

        val markker9 = MarkerData()
        markker9.latitue = 30.701967
        markker9.longitude = 76.6621014
        markker9.title = "Phase 8 Park"
        markersArray.add(markker9)
        val markker10 = MarkerData()
        markker10.latitue = 30.701967
        markker10.longitude = 76.6621014
        markker10.title = "Phase 8 Park"
        markersArray.add(markker10)

        val markker11 = MarkerData()
        markker11.latitue = 30.7048045
        markker11.longitude = 76.63574744
        markker11.title = "Sohana Hospital"
        markersArray.add(markker11)


        val markker12 = MarkerData()
        markker12.latitue = 30.7048045
        markker12.longitude = 76.6357474
        markker12.title = "Sohana Killa"
        markersArray.add(markker12)





        for (i in 0 until markersArray.size) {

            createMarker(
                markersArray[i]?.latitue!!,
                markersArray[i].longitude!!,
                markersArray[i].title!!,
                R.drawable.map_location

            )
        }


        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    30.701967,
                    76.6621014
                ), 18.0f
            )
        )
    }

    protected fun createMarker(
        latitude: Double,
        longitude: Double,
        title: String,
        iconResID: Int
    ): Marker {

        return mMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet("This is your Place")
                .icon(BitmapDescriptorFactory.fromResource(iconResID))
        );

    }


}
