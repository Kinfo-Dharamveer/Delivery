package com.jointdelivery.utilities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CommonResponseModel(){

    @SerializedName("Success")
    @Expose
    internal var success: Boolean = false
    @SerializedName("Message")
    @Expose
    internal var message: String = ""
}