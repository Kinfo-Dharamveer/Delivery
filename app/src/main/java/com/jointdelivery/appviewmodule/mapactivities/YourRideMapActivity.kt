package com.jointdelivery.appviewmodule.mapactivities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.admin.maps.DataParser
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.jointdelivery.CustomInfoWindowGoogleMap
import com.jointdelivery.InfoWindowData

import com.jointdelivery.R
import kotlinx.android.synthetic.main.tool_bar_layout.*
import kotlinx.android.synthetic.main.user_detail_layout.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

open class YourRideMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    internal lateinit var MarkerPoints: ArrayList<LatLng>
    private lateinit var mMap: GoogleMap
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    internal var mLastLocation: Location? = null
    internal var mCurrLocationMarker: Marker? = null
    var isExtraDataShowing = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_ride_layout)

        toolbar_title.text = "Your Ride"

        back_button.visibility = View.VISIBLE

        back_button.setOnClickListener {
            onBackPressed()
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        MarkerPoints = ArrayList<LatLng>()



        show_details.setOnClickListener {
            if (isExtraDataShowing) {
                show_details.text = "Show Detail"
                detail_text_view.visibility = View.GONE
            } else {
                show_details.text = "Hide Detail"
                detail_text_view.visibility = View.VISIBLE
            }
            isExtraDataShowing = !isExtraDataShowing
        }


    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap?.getUiSettings()?.setMapToolbarEnabled(false);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
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


        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 120000 // two minute interval
        mLocationRequest.fastestInterval = 120000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
                mMap.setMyLocationEnabled(true)
            } else {
                //Request Location Permission
                checkLocationPermission()
            }
        } else {
            mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            mMap.setMyLocationEnabled(true)
        }

        if (mLastLocation != null) {
            val yourposition = LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)
            mMap.addMarker(MarkerOptions().position(yourposition).title("your position"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(yourposition))


            mMap.setOnMapClickListener { point ->
                // Already two locations
                if (MarkerPoints.size > 1) {
                    MarkerPoints.clear()
                    mMap.clear()
                }

                // Adding new item to the ArrayList
                MarkerPoints.add(point)

                // Creating MarkerOptions
                val options = MarkerOptions()

                // Setting the position of the marker
                options.position(point)

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (MarkerPoints.size == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                } else if (MarkerPoints.size == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                }


                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options)

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size >= 2) {
                    val origin = MarkerPoints[0]
                    val dest = MarkerPoints[1]

                    // Getting URL to the Google Directions API
                    val url = getUrl(origin, dest)
                    Log.d("onMapClick", url.toString())
                    val FetchUrl = FetchUrl()

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url)
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin))
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
                }
            }
        }



    }

    public override fun onPause() {
        super.onPause()

        if (mFusedLocationClient != null) {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
        }
    }


    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                //The last location in the list is the newest
                val location = locationList[locationList.size - 1]
                Log.i("MapsActivity", "Location: " + location.latitude + " " + location.longitude)
                mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker!!.remove()
                }

                //Place current location marker
                val latLng = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title("Current Position")
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                mCurrLocationMarker = mMap.addMarker(markerOptions)

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        mFusedLocationClient?.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper()
                        )
                        mMap.setMyLocationEnabled(true)
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK") { dialogInterface, i ->
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this@YourRideMapActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }


    fun contactCustomer(view: View) {

    }

    fun sendMessage(view: View) {

    }


    fun share(view: View) {

        val intent = Intent(
            android.content.Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?saddr=30.7350626,76.6934885 &daddr=9.5448732,76.7719862")
        )
        startActivity(intent)
    }


    private fun getUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude


        // Sensor enabled
        val sensor = "sensor=false"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor"

        // Output format
        val output = "json"

        // Building the url to the web service
        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"


        return url
    }

    // Fetches data from url passed
    private inner class FetchUrl : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg url: String): String {

            // For storing data from web service
            var data = ""

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0])
                Log.d("Background Task data", data.toString())
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())
            }

            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            val parserTask = ParserTask()

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result)

        }
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            // Creating an http connection to communicate with url
            urlConnection = url.openConnection() as HttpURLConnection

            // Connecting to url
            urlConnection.connect()

            // Reading data from url
            iStream = urlConnection.inputStream

            val br = BufferedReader(InputStreamReader(iStream!!))

            val sb = StringBuffer()

            var line = ""
//            while ((line = br.readLine()) != null) {
//                sb.append(line)
//            }
            while (line != null) {
                line = br.readLine()
                sb.append(line)
            }

            data = sb.toString()
            Log.d("downloadUrl", data.toString())
            br.close()

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    private inner class ParserTask : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

        // Parsing the data in non-ui thread
        override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>> {

            val jObject: JSONObject

            try {
                jObject = JSONObject(jsonData[0])
                Log.d("ParserTask", jsonData[0])
                val parser = DataParser()
                Log.d("ParserTask", parser.toString())

                // Starts parsing data
                var routes: List<List<HashMap<String, String>>> = parser.parse(jObject)
                Log.d("ParserTask", "Executing routes")
                Log.d("ParserTask", routes.toString())
                return routes

            } catch (e: Exception) {
                Log.d("ParserTask", e.toString())
                e.printStackTrace()
            }

            val r: List<List<HashMap<String, String>>> = ArrayList<ArrayList<HashMap<String, String>>>()
            return r
        }

        // Executes in UI thread, after the parsing process
        override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
            var points: ArrayList<LatLng>
            var lineOptions: PolylineOptions? = null

            // Traversing through all the routes
            for (i in result.indices) {
                points = ArrayList<LatLng>()
                lineOptions = PolylineOptions()

                // Fetching i-th route
                val path = result[i]

                // Fetching all the points in i-th route
                for (j in path.indices) {
                    val point = path[j]

                    val lat = java.lang.Double.parseDouble(point["lat"])
                    val lng = java.lang.Double.parseDouble(point["lng"])
                    val position = LatLng(lat, lng)

                    points.add(position)
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(10f)
                lineOptions.color(Color.RED)

                Log.d("onPostExecute", "onPostExecute lineoptions decoded")

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions)
            } else {
                Log.d("onPostExecute", "without Polylines drawn")
            }
        }
    }

}