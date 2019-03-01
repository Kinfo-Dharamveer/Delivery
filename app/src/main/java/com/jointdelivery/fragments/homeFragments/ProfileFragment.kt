package com.jointdelivery.fragments.homeFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.appviewmodule.loginactivities.LoginActivity
import com.jointdelivery.appviewmodule.loginactivities.ProfileEditActivity
import com.jointdelivery.appviewmodule.profileupdateApi.ProfileModel
import com.jointdelivery.appviewmodule.profileupdateApi.ProfileUpdateMethods
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.basemodule.activities.HomeActivity
import com.jointdelivery.interfaces.SettingClickListener
import com.jointdelivery.utilities.CommonUtil
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import kotlinx.android.synthetic.main.profile_layout.*

import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

open class ProfileFragment : Fragment(), PostAPIResultCallback<ProfileModel>, SettingClickListener {
    val profileDetail: HashMap<String, String> = HashMap<String, String>() //define empty hashmap

    override fun onSettingClick() {
        val intent = Intent(activity!!, ProfileEditActivity::class.java)
        intent.putExtra("profileDetail", profileDetail)
        startActivity(intent)
    }

    @Inject
    lateinit var authManager: AuthManager

    override fun onResponse(response: Response<ProfileModel>) {
        CommonUtil.hideProgress()
        setProfileDataIntoView(response)
    }

    private fun setProfileDataIntoView(response: Response<ProfileModel>) {

        try {
            text_vehicleType.text = response.body()?.data?.get(0)?.vehicleType
            text_vehicle_No.text = response.body()?.data?.get(0)?.vehiclePlateNumber
            text_vehicle_brand.text = response.body()?.data?.get(0)?.vehicleBrand
            text_vehicle_color.text = response.body()?.data?.get(0)?.vehicleColour
            text_vehicle_year.text = response.body()?.data?.get(0)?.vehicleMakingYear

            text_user_name.text = response.body()?.data?.get(0)?.name
            text_email.text = response.body()?.data?.get(0)?.email
            text_phone.text = response.body()?.data?.get(0)?.phoneNumber
            text_address.text = response.body()?.data?.get(0)?.address

            Glide.with(this)
                .load(response.body()?.data?.get(0)?.profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(image_profile)

            profileDetail.put("name", response.body()?.data?.get(0)?.name!!)
            profileDetail.put("email", response.body()?.data?.get(0)?.email!!)
            profileDetail.put("phoneNumber", response.body()?.data?.get(0)?.phoneNumber!!)
            profileDetail.put("state", response.body()?.data?.get(0)?.state!!)
            profileDetail.put("city", response.body()?.data?.get(0)?.city!!)
            profileDetail.put("address", response.body()?.data?.get(0)?.address!!)
            profileDetail.put("zipcode", response.body()?.data?.get(0)?.zipCode!!)
            profileDetail.put("profilePictureUrl", response.body()?.data?.get(0)?.profilePictureUrl!!)
            profileDetail.put("vehicleType", response.body()?.data?.get(0)?.vehicleType!!)
            profileDetail.put("vehiclePlateNumber", response.body()?.data?.get(0)?.vehiclePlateNumber!!)
            profileDetail.put("vehicleBrand", response.body()?.data?.get(0)?.vehicleBrand!!)
            profileDetail.put("vehicleColour", response.body()?.data?.get(0)?.vehicleColour!!)
            profileDetail.put("vehicleMakingYear", response.body()?.data?.get(0)?.vehicleMakingYear!!)
            profileDetail.put("licenseNumber", response.body()?.data?.get(0)?.licenseNumber!!)
            profileDetail.put("vehicleFrontPhoto", response.body()?.data?.get(0)?.vehicleFrontPhoto!!)
            profileDetail.put("vehicleBackPhoto", response.body()?.data?.get(0)?.vehicleBackPhoto!!)



        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        getProfileData()
    }

    override fun onFailure() {
        CommonUtil.hideProgress()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity!!.application as MyApplication).getAppComponent()?.inject(this)

        (activity as HomeActivity).setListener(this)

        return inflater.inflate(R.layout.profile_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_sign_out.setOnClickListener {
            authManager.clearPrefernces()
            val intent = Intent(activity!!, LoginActivity::class.java)
            startActivity(intent)
            activity!!.finishAffinity()

        }
    }

    fun getProfileData() {
        CommonUtil.showProgress(activity!!)
        ProfileUpdateMethods.getProfileData(authManager.getAccessToken(), authManager.getUserId(), this)
    }


}