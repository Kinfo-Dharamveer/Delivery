package com.jointdelivery.appviewmodule.registeractivities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("State")
    @Expose
    var state: String? = null
    @SerializedName("City")
    @Expose
    var city: String? = null
    @SerializedName("ZipCode")
    @Expose
    var zipCode: String? = null

}