package com.scyhealth.loginmodule

import com.jointdelivery.appviewmodule.model.ForgotPasswordModel
import com.jointdelivery.appviewmodule.model.LoginModel
import com.jointdelivery.appviewmodule.model.SignUpPhoneModel
import com.jointdelivery.appviewmodule.registeractivities.ZipCodeData
import com.jointdelivery.utilities.CommonResponseModel
import retrofit2.Call
import retrofit2.http.*

interface LoginApiInterface {

    @FormUrlEncoded
    @POST("api/Account/Register")
    fun register(
        @Field("FirstName") firstName: String, @Field("LastName") lastName: String, @Field("email") email: String,
        @Field("password") password: String, @Field("ConfirmPassword") confirmPassword: String, @Field("roleId") roleId: Int,
        @Field("Speciality") speciality: String, @Field("Experience") experience: Float
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("Token")
    fun login(
        @Field("Username") userName: String, @Field("Password") password: String, @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String, @Field("client_secret") clientSecret: String, @Field("AccountId") accountId: String
    ): Call<LoginModel>

    @FormUrlEncoded
    @POST("Token")
    fun refreshToken(
        @Field("refresh_token") refreshToken: String, @Field("grant_type") grantType: String, @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String, @Field("AccountId") accountId: String
    ): Call<LoginModel>

    @FormUrlEncoded
    @POST("api/Account/ForgotPassword")
    fun forgotPassword(
        @Field("email") email: String
    ): Call<ForgotPasswordModel>

    @FormUrlEncoded
    @POST("api/Account/ResetPassword")
    fun resetPassword(
        @Field("Email") email: String, @Field("NewPassword") newPassword: String, @Field("ConfirmPassword") confirmPassword: String,
        @Field("Token") token: String, @Field("OTP") otp: String
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("api/Account/SetSecurityPin")
    fun setSecurityPin(
        @Field("Pin") pin: String, @Field("ConfirmPin") confirmPin: String
    ): Call<CommonResponseModel>


    @POST("api/Account/InitSignupByPhone")
    fun reisterWithPhoneNumber(@Query("phoneNo") phonenumber: String): Call<SignUpPhoneModel>

    @POST("api/Account/SignupVerifyOTP")
    fun verifyOtpWithPhoneNumber(@Query("phoneNo") phonenumber: String, @Query("otp") otp: String, @Query("code") code: String): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("api/Account/DriverSignup")
    fun registerUserDetails(@FieldMap userData: Map<String, String>): Call<CommonResponseModel>


    @GET("api/Location/VerifyZipCode")
    fun getZipCodeData(@Query("zipCode") zipCode: String):Call<ZipCodeData>

    @FormUrlEncoded
    @POST("api/Driver/CreateDriverProfile")
    fun registerUserDetailsAndVehicleDetails(@FieldMap vehicledriverInfo: Map<String, String>): Call<CommonResponseModel>

}
