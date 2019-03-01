package com.jointdelivery.appviewmodule.profileupdateApi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("State")
    @Expose
    var state: String? = null
    @SerializedName("City")
    @Expose
    var city: String? = null
    @SerializedName("ZipCode")
    @Expose
    var zipCode: String? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("ProfilePictureUrl")
    @Expose
    var profilePictureUrl: String? = null
    @SerializedName("PhoneNumber")
    @Expose
    var phoneNumber: String? = null
    @SerializedName("Email")
    @Expose
    var email: String? = null
    @SerializedName("LicenseNumber")
    @Expose
    var licenseNumber: String? = null
    @SerializedName("Address")
    @Expose
    var address: String? = null
    @SerializedName("VehicleType")
    @Expose
    var vehicleType: String? = null
    @SerializedName("VehiclePlateNumber")
    @Expose
    var vehiclePlateNumber: String? = null
    @SerializedName("VehicleRegistrationNumber")
    @Expose
    var vehicleRegistrationNumber: String? = null
    @SerializedName("VehicleBrand")
    @Expose
    var vehicleBrand: String? = null
    @SerializedName("VehicleMakingYear")
    @Expose
    var vehicleMakingYear: String? = null
    @SerializedName("VehicleColour")
    @Expose
    var vehicleColour: String? = null
    @SerializedName("VehicleFrontPhoto")
    @Expose
    var vehicleFrontPhoto: String? = null
    @SerializedName("VehicleBackPhoto")
    @Expose
    var vehicleBackPhoto: String? = null

}