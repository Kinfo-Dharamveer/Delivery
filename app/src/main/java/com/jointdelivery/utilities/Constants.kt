package com.jointdelivery.utilities

import java.util.regex.Pattern

class Constants {

    companion object {
        val CLIENT_ID = "JointDelivery"
        val CLIENT_SECRET = "Abc@123"
        val PASSWORD = "password"

        val KEY_ACCESS_TOKEN = "accessToken"

        val KEY_USER_ID = "userId"
        val KEY_FIRST_NAME = "firstName"
        val KEY_LAST_NAME = "lastName"
        val KEY_ROLE = "role"
        val KEY_PIN = "pin"
        val KEY_PERMANENT_PASSWORD = "permanentPassword"
        val KEY_PROFILE_PIC = "profilePic"
        val KEY_PHONE = "phone"

        val KEY_CODE = "code"

        val KEY_ID = "keyId"
        val KEY_NAME = "keyName"
        val KEY_EMAIL = "keyEmail"
        val REFRESH_TOKEN = "refresh_token"
        val KEY_REFRESH_TOKEN = "refreshToken"
        val EMAIL_ADDRESS_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
        val REGISTERED_PHONE_NO = "registeredPhoneNumber"
        val GRANT_TYPE= "password"
        val IS_PROFILE_REGISTERED="isProfileregisterd"
        val BACKGROUND_PROCESS="backgroundProcess"

        // AWS Constants
        val COGNITO_POOL_ID = "eu-west-1:375c3ab7-05cd-49a2-94a0-cda5935112c7"
        val BUCKET_NAME = "scyphirhealth"
        val BUCKET_REGION = "eu-west-1"
        var PERMISSION_ALL = 1
        var CAMERA_PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        )
    }
}