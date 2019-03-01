package com.scyhealth.loginmodule

import com.jointdelivery.auth.ApiClient
import com.jointdelivery.auth.ApiClientWithoutHeader
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.appviewmodule.model.ForgotPasswordModel
import com.jointdelivery.appviewmodule.model.LoginModel
import com.jointdelivery.appviewmodule.model.SignUpPhoneModel
import com.jointdelivery.appviewmodule.registeractivities.ZipCodeData
import com.jointdelivery.utilities.CommonResponseModel
import com.jointdelivery.utilities.Constants
import com.scyhealth.basemodule.interfaces.GetAPIResultCallback
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginApiMethods() {

    companion object {

        var mLoginApiInterface: LoginApiInterface = ApiClientWithoutHeader.client.create(LoginApiInterface::class.java)

        fun register(
            mPostAPIResultCallback: PostAPIResultCallback<CommonResponseModel>, firstName: String, lastName: String,
            email: String, password: String, roleId: Int, speciality: String, experience: Float
        ) {
            mLoginApiInterface.register(firstName, lastName, email, password, password, roleId, speciality, experience)
                .enqueue(object : Callback<CommonResponseModel> {
                    override fun onResponse(call: Call<CommonResponseModel>, response: Response<CommonResponseModel>) {
                        mPostAPIResultCallback.onResponse(response)
                    }

                    override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                        mPostAPIResultCallback.onFailure()
                    }
                })
        }

        fun login(
            mPostAPIResultCallback: PostAPIResultCallback<LoginModel>,
            email: String, password: String, grantType: String
        ) {

            mLoginApiInterface.login(email, password, grantType, Constants.CLIENT_ID, Constants.CLIENT_SECRET,"2")
                .enqueue(object : Callback<LoginModel> {
                    override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                        mPostAPIResultCallback.onResponse(response)
                    }

                    override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                        mPostAPIResultCallback.onFailure()
                    }
                })
        }

        fun refreshToken(
            mPostAPIResultCallback: PostAPIResultCallback<LoginModel>,
            refreshToken: String,
            grantType: String
        ) {

            mLoginApiInterface.refreshToken(refreshToken, grantType, Constants.CLIENT_ID, Constants.CLIENT_SECRET,"2")
                .enqueue(object : Callback<LoginModel> {
                    override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                        mPostAPIResultCallback.onResponse(response)
                    }

                    override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                        mPostAPIResultCallback.onFailure()
                    }
                })
        }

        fun forgotPassword(mPostAPIResultCallback: PostAPIResultCallback<ForgotPasswordModel>, email: String) {
            mLoginApiInterface.forgotPassword(email)
                .enqueue(object : Callback<ForgotPasswordModel> {
                    override fun onResponse(call: Call<ForgotPasswordModel>, response: Response<ForgotPasswordModel>) {
                        mPostAPIResultCallback.onResponse(response)
                    }

                    override fun onFailure(call: Call<ForgotPasswordModel>, t: Throwable) {
                        mPostAPIResultCallback.onFailure()
                    }
                })
        }

        fun resetPassword(
            mPostAPIResultCallback: PostAPIResultCallback<CommonResponseModel>, email: String, newPassword: String,
            confirmPassword: String, token: String, otp: String
        ) {
            mLoginApiInterface.resetPassword(email, newPassword, confirmPassword, token, otp)
                .enqueue(object : Callback<CommonResponseModel> {
                    override fun onResponse(call: Call<CommonResponseModel>, response: Response<CommonResponseModel>) {
                        mPostAPIResultCallback.onResponse(response)
                    }

                    override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                        mPostAPIResultCallback.onFailure()
                    }
                })
        }

        fun setSecurityPin(
            mPostAPIResultCallback: PostAPIResultCallback<CommonResponseModel>,
            securityPin: String, authManager: AuthManager) {

            val auth_key = "bearer " + authManager.getAccessToken()
            val service = ApiClient.createService(LoginApiInterface::class.java, auth_key)

            service.setSecurityPin(securityPin, securityPin)
                .enqueue(object : Callback<CommonResponseModel> {
                    override fun onResponse(call: Call<CommonResponseModel>, response: Response<CommonResponseModel>) {
                        mPostAPIResultCallback.onResponse(response)
                    }

                    override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                        mPostAPIResultCallback.onFailure()
                    }
                })
        }

        fun registerWithPhoneNumber(
            mPostAPIResultCallback: PostAPIResultCallback<SignUpPhoneModel>,
            phonenumber: String
        ) {
            mLoginApiInterface.reisterWithPhoneNumber(phonenumber).enqueue(object : Callback<SignUpPhoneModel> {
                override fun onFailure(call: Call<SignUpPhoneModel>, t: Throwable) {
                    mPostAPIResultCallback.onFailure()

                }

                override fun onResponse(call: Call<SignUpPhoneModel>, response: Response<SignUpPhoneModel>) {
                    mPostAPIResultCallback.onResponse(response)
                }

            })
        }

        fun verifyOtpWithPhoneNumber(
            phonenumber: String,
            otp: String,code:String,
            postAPIResultCallback: PostAPIResultCallback<CommonResponseModel>
        ) {

            mLoginApiInterface.verifyOtpWithPhoneNumber(phonenumber, otp, code)
                .enqueue(object : Callback<CommonResponseModel> {
                    override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                        postAPIResultCallback.onFailure()
                    }

                    override fun onResponse(call: Call<CommonResponseModel>, response: Response<CommonResponseModel>) {
                        postAPIResultCallback.onResponse(response)
                    }

                })

        }

        fun  getZipCodedata(zipCode: String, getAPIResultCallback: GetAPIResultCallback<ZipCodeData>){
            mLoginApiInterface.getZipCodeData(zipCode).enqueue(object :Callback<ZipCodeData>{
                override fun onFailure(call: Call<ZipCodeData>, t: Throwable) {
                    getAPIResultCallback.onGetFailure()
                }

                override fun onResponse(call: Call<ZipCodeData>, response: Response<ZipCodeData>) {
                    getAPIResultCallback.onGetResponse(response)
                }

            })
        }

        fun registerUserInfo(
            postAPIResultCallback: PostAPIResultCallback<CommonResponseModel>,
            userDetails: HashMap<String, String>
        ) {
            mLoginApiInterface.registerUserDetails(userDetails).enqueue(object :Callback<CommonResponseModel>{
                override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                    postAPIResultCallback.onFailure()
                }

                override fun onResponse(call: Call<CommonResponseModel>, response: Response<CommonResponseModel>) {
               postAPIResultCallback.onResponse(response)
                }

            })
        }



        fun registerUserDetailsAndVehicleDetails(
            postAPIResultCallback: PostAPIResultCallback<CommonResponseModel>,
            vehicledriverInfo: HashMap<String, String>
        ) {
            mLoginApiInterface.registerUserDetailsAndVehicleDetails(vehicledriverInfo).enqueue(object :Callback<CommonResponseModel>{
                override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                    postAPIResultCallback.onFailure()
                }

                override fun onResponse(call: Call<CommonResponseModel>, response: Response<CommonResponseModel>) {
                    postAPIResultCallback.onResponse(response)
                }

            })
        }


    }

}