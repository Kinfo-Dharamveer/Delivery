package com.jointdelivery.auth

import android.content.SharedPreferences
import com.jointdelivery.utilities.Constants
import dagger.Module

import javax.inject.Inject
import android.R.attr.keySet
import android.R.attr.keySet





@Module
class AuthManager @Inject
constructor(private val mPreference: SharedPreferences) {

    fun clearPrefernces() {
        mPreference.edit().clear().apply()
    }

    fun saveMyDetails(id: Int, name: String, email: String) {
        mPreference.edit().putInt(Constants.KEY_ID, id).apply()
        mPreference.edit().putString(Constants.KEY_NAME, name).apply()
        mPreference.edit().putString(Constants.KEY_EMAIL, email).apply()
    }

    fun getUserId(): Int {
        return mPreference.getInt(Constants.KEY_USER_ID, 0)
    }

    fun getUserName(): String {
        return mPreference.getString(Constants.KEY_NAME, "") ?: ""
    }

    fun getUserEmail(): String {
        return mPreference.getString(Constants.KEY_EMAIL, "") ?: ""
    }

    companion object {
        private val TAG = AuthManager::class.java.simpleName
    }

    fun setPermanentPassword(permanentPassword: String) {
        mPreference.edit().putString(Constants.KEY_PERMANENT_PASSWORD, permanentPassword).apply()
    }

    fun getPermanentPassword(): String {
        return mPreference.getString(Constants.KEY_PERMANENT_PASSWORD, "") ?: ""
    }
    fun setEmail(email: String) {
        mPreference.edit().putString(Constants.KEY_EMAIL, email).apply()
    }

    fun getEmail(): String {
        return mPreference.getString(Constants.KEY_EMAIL, "") ?: ""
    }

    fun setRefreshToken(refreshToken: String) {
        mPreference.edit().putString(Constants.KEY_REFRESH_TOKEN, refreshToken).apply()
    }

    fun getRefreshToken(): String {
        return mPreference.getString(Constants.KEY_REFRESH_TOKEN, "") ?: ""
    }

    fun setAccessToken(accessToken: String) {
        mPreference.edit().putString(Constants.KEY_ACCESS_TOKEN, accessToken).apply()
    }

    fun getAccessToken(): String {
        return mPreference.getString(Constants.KEY_ACCESS_TOKEN, "") ?: ""
    }

    fun setUserId(userId: Int) {
        mPreference.edit().putInt(Constants.KEY_USER_ID, userId).apply()

    }

    fun setFirstName(firstName: String) {
        mPreference.edit().putString(Constants.KEY_FIRST_NAME, firstName).apply()
    }

    fun setLastName(lastName: String) {
        mPreference.edit().putString(Constants.KEY_LAST_NAME, lastName).apply()
    }

    fun setRole(role: String) {
        mPreference.edit().putString(Constants.KEY_ROLE, role).apply()
    }



    fun setPermanentPassword(toString: Any) {

    }

    fun setRegisteredPhoneNumber(phoneNumber: String) {
    mPreference.edit().putString(Constants.REGISTERED_PHONE_NO,phoneNumber).apply()
    }
    fun getRegisteredPhoneNumber(): String {
        return mPreference.getString(Constants.REGISTERED_PHONE_NO, "") ?: ""
    }

    fun setDriverProfileDetails(hashMap: HashMap<String, String>) {
        var aMap = HashMap<String, String>()
         aMap = hashMap;
        for (s in aMap.keys) {
            mPreference.edit().putString(s, aMap.get(s)).apply()
        }

        mPreference.edit().putBoolean(Constants.IS_PROFILE_REGISTERED,true).apply()
    }

    fun setBackgroundProcessDone(background: Boolean) {
        mPreference.edit().putBoolean(Constants.BACKGROUND_PROCESS,background).apply()
    }

    fun getBackgroundProcessDone(): Boolean {
        return mPreference.getBoolean(Constants.BACKGROUND_PROCESS, false)
    }
}
