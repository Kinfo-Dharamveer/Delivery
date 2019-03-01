package com.jointdelivery.appviewmodule.profileupdateApi

import com.jointdelivery.utilities.CommonResponseModel
import retrofit2.Call
import retrofit2.http.*

interface ProfileUpdateInterface {


    @FormUrlEncoded
    @POST("api/Driver/UpdateProfile")
    fun updateDriverProfile(@FieldMap profileData: Map<String, String>): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("/api/Driver/UpdateVehicleData")
    fun updateVehicleDetails(@FieldMap vehicleData: Map<String, String>): Call<CommonResponseModel>

    @GET("api/Driver/GetProfile")
    fun getProfileData(@Query("id") id: Int): Call<ProfileModel>
}