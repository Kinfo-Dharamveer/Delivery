package com.jointdelivery.appviewmodule.registeractivities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jointdelivery.R
import com.jointdelivery.utilities.CommonResponseModel
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import retrofit2.Response
import android.content.Intent
import android.graphics.Color
import android.widget.AdapterView
import com.bruce.pickerview.popwindow.DatePickerPopWin
import com.jointdelivery.MyApplication
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.appviewmodule.loginactivities.LoginActivity
import com.jointdelivery.utilities.CommonUtil
import com.scyhealth.loginmodule.LoginApiMethods
import kotlinx.android.synthetic.main.activity_driver_info_layout.*
import kotlinx.android.synthetic.main.tool_bar_layout.*
import javax.inject.Inject


open class DriverInfoActivity : AppCompatActivity(), PostAPIResultCallback<CommonResponseModel> {
    @Inject
    lateinit var authManager: AuthManager



    override fun onResponse(response: Response<CommonResponseModel>) {
        CommonUtil.hideProgress()
        if (response.body()!!.success) {
            CommonUtil.showShortToast(applicationContext, response.body()!!.message)
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        } else {
            CommonUtil.showSnackbar(main_layout, response.body()!!.message)
        }
    }

    override fun onFailure() {
        CommonUtil.hideProgress()
        CommonUtil.showSnackbar(main_layout, resources.getString(R.string.something_wrong))
    }


    var selected_driver_state = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.jointdelivery.R.layout.activity_driver_info_layout)
        (application as MyApplication).getAppComponent()?.inject(this)
        toolbar_title.text = resources.getString(R.string.driver_info)
        spinnerinit();
        (application as MyApplication).getAppComponent()?.inject(this)

        back_button.setOnClickListener { finish() }
    }

    private fun spinnerinit() {



        spinner_driver_state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0)
                    selected_driver_state = spinner_driver_state.selectedItem.toString()
            }

        }

    }

    fun StartBackgroundCheckProcess(view: View) {

        if (CommonUtil.isConnectingToInternet(applicationContext)) {

            if (CommonUtil.checkValidEmail(editText_driver_name)) {
                CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_name))
                return
            }

            if (edt_driver_Dob.text.toString().equals("")) {
                CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_dob))
                return
            }

            if (CommonUtil.checkValidEmail(edt_security_number)) {
                CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_edt_security_number))

                return
            }

            if (selected_driver_state.equals("")) {
                CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_selete_state))

                return
            }
            val vehicledriverInfo = intent.getSerializableExtra("HashVehicleDetails") as HashMap<String, String>

            vehicledriverInfo.put("DateOfBirth", edt_driver_Dob.text.toString())
            vehicledriverInfo.put("SocialSecurityNumber", edt_security_number.text.toString())
            vehicledriverInfo.put("DriverLicenseState", selected_driver_state)
            vehicledriverInfo.put("DriverLicenseNumber", editText_driver_licenecNo.text.toString())
            vehicledriverInfo.put("AccountId", "2")

            CommonUtil.showProgress(this)
            LoginApiMethods.registerUserInfo(this, vehicledriverInfo)


        } else {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))

        }
    }

}