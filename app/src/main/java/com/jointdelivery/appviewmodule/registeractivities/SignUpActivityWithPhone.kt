package com.jointdelivery.appviewmodule.registeractivities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.appviewmodule.loginactivities.LoginActivity
import com.jointdelivery.appviewmodule.loginactivities.OtpScreenActivity
import com.jointdelivery.appviewmodule.model.SignUpPhoneModel
import com.jointdelivery.utilities.CommonUtil
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import com.scyhealth.loginmodule.LoginApiMethods
import kotlinx.android.synthetic.main.activity_sign_up_layout.*
import retrofit2.Response
import javax.inject.Inject

class SignUpActivityWithPhone : AppCompatActivity(), PostAPIResultCallback<SignUpPhoneModel> {

    @Inject
    lateinit var authManager: AuthManager

    lateinit var dialog: AlertDialog

    override fun onResponse(response: Response<SignUpPhoneModel>) {

        CommonUtil.hideProgress()
        if (response.body()!!.success) {

            authManager.setRegisteredPhoneNumber(edit_text_phone.text.toString())
            CommonUtil.showShortToast(applicationContext, response.body()!!.message)
            val intent: Intent
            intent = Intent(this, OtpScreenActivity::class.java)
            intent.putExtra("code", response.body()!!.code)
            startActivity(intent)

        } else {
            CommonUtil.showShortToast(applicationContext, response.body()!!.message)
        }
    }

    override fun onFailure() {

        CommonUtil.hideProgress()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_layout)
        (application as MyApplication).getAppComponent()?.inject(this)
//        scroll_layout.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                when (event?.action) {
//                    MotionEvent.ACTION_DOWN -> CommonUtil.hideKeyboard(v!!, applicationContext)
//                }
//
//                return v?.onTouchEvent(event) ?: true
//            }
//        })
    }

    fun Login(view: View) {
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun joinNow(view: View) {

        if (!CommonUtil.isConnectingToInternet(this)) {

            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
            return
        }
        if (CommonUtil.checkEmptyString(edit_text_phone)) {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_enter_phone))
            return
        }
        CommonUtil.showProgress(this)
        LoginApiMethods.registerWithPhoneNumber(this, edit_text_phone.text.toString())

    }
}