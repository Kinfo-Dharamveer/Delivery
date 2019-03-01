package com.jointdelivery.fragments.homeFragments

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.jointdelivery.R
import com.google.android.gms.maps.model.*
import com.jointdelivery.basemodule.models.MarkerData
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_your_routes.*
import com.google.android.gms.maps.GoogleMap
import com.jointdelivery.CustomInfoWindowGoogleMap
import com.jointdelivery.InfoWindowData


class YourRoutesFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        //Disable Map Toolbar:
        mMap?.getUiSettings()?.setMapToolbarEnabled(false);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = mMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    activity, R.raw.map_style
                )
            )

            if (!success!!) {
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

        var customInfoWindow = CustomInfoWindowGoogleMap(activity!!);
        mMap?.setInfoWindowAdapter(customInfoWindow);


        mMap?.addMarker(markerOptions);
        var m: Marker = mMap?.addMarker(markerOptions)!!;
        m.setTag(info);
        m.showInfoWindow();

        mMap?.moveCamera(CameraUpdateFactory.newLatLng(snowqualmie));
        addmarkerToMapViiew()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_your_routes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)




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


        mMap?.animateCamera(
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

        return mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet("This is your Place")
                .icon(BitmapDescriptorFactory.fromResource(iconResID))
        );

    }


}