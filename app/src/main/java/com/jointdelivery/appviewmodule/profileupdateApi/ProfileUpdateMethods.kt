package com.jointdelivery.appviewmodule.profileupdateApi

import com.jointdelivery.auth.ApiClient
import com.jointdelivery.utilities.CommonResponseModel
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUpdateMethods {
    companion object {


        fun updateDriverProfile(
            mPostAPIResultCallback: PostAPIResultCallback<CommonResponseModel>,
            auth_key: String,
            userDetails: HashMap<String, String>
        ) {
            val service = ApiClient.createService(ProfileUpdateInterface::class.java, auth_key)
            service.updateDriverProfile(userDetails)
                .enqueue(object : Callback<CommonResponseModel> {
                    override fun onResponse(call: Call<CommonResponseModel>, response: Response<CommonResponseModel>) {
                        mPostAPIResultCallback.onResponse(response)
                    }

                    override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                        mPostAPIResultCallback.onFailure()
                    }
                })
        }


        fun updateVehicleDetails(
            mPostAPIResultCallback: PostAPIResultCallback<CommonResponseModel>,
            auth_key: String,
            vehicleDetails: HashMap<String, String>
        ) {
            val service = ApiClient.createService(ProfileUpdateInterface::class.java, auth_key)
            service.updateVehicleDetails(vehicleDetails)
                .enqueue(object : Callback<CommonResponseModel> {
                    override fun onResponse(call: Call<CommonResponseModel>, response: Response<CommonResponseModel>) {
                        mPostAPIResultCallback.onResponse(response)
                    }

                    override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                        mPostAPIResultCallback.onFailure()
                    }
                })
        }


        fun getProfileData(auth_key: String, id: Int, mPostAPIResultCallback: PostAPIResultCallback<ProfileModel>) {
            val service = ApiClient.createService(ProfileUpdateInterface::class.java, auth_key)

            service.getProfileData(id).enqueue(object : Callback<ProfileModel> {
                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                    mPostAPIResultCallback.onFailure()
                }

                override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                    mPostAPIResultCallback.onResponse(response)
                }

            })
        }
    }
}
