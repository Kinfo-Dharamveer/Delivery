package com.jointdelivery.appviewmodule.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class LoginModel{

    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null
    @SerializedName("token_type")
    @Expose
    var tokenType: String? = null
    @SerializedName("expires_in")
    @Expose
    var expiresIn: Int? = null
    @SerializedName("refresh_token")
    @Expose
    var refreshToken: String? = null
    @SerializedName("as:client_id")
    @Expose
    var asClientId: String? = null

    @SerializedName("userName")
    @Expose
    var userName: String? = null
    @SerializedName("firstName")
    @Expose
    var firstName: String? = null
    @SerializedName("lastName")
    @Expose
    var lastName: String? = null
    @SerializedName("userId")
    @Expose
    var userId: String? = null
    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("isActive")
    @Expose
    var isActive: String? = null

    @SerializedName(".issued")
    @Expose
    var issued: String? = null
    @SerializedName(".expires")
    @Expose
    var expires: String? = null

    @SerializedName("EmailConfirmed")
    @Expose
    var emailConfirmed: String? = null
}