package com.jointdelivery.appviewmodule.registeractivities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ZipCodeData {

    @SerializedName("Success")
    @Expose
    var success: Boolean? = null
    @SerializedName("Data")
    @Expose
    var data: Data? = null
    @SerializedName("Message")
    @Expose
    var message: String? = null

}