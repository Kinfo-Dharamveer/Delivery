package com.jointdelivery.appviewmodule.profileupdateApi
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jointdelivery.appviewmodule.profileupdateApi.Datum




class ProfileModel {

    @SerializedName("Success")
    @Expose
    var success: Boolean? = null
    @SerializedName("Data")
    @Expose
    var data: List<Datum>? = null
    @SerializedName("Message")
    @Expose
    var message: String? = null

}