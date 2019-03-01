package com.jointdelivery.appviewmodule.loginactivities

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.basemodule.activities.BaseActivity
import com.jointdelivery.utilities.CommonResponseModel
import com.jointdelivery.utilities.CommonUtil
import com.jointdelivery.utilities.Constants
import com.scyhealth.loginmodule.LoginApiMethods
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import kotlinx.android.synthetic.main.activity_reset_activity.*
import kotlinx.android.synthetic.main.tool_bar_layout.*
import retrofit2.Response
import javax.inject.Inject

class ResetPasswordActivity : BaseActivity(), PostAPIResultCallback<CommonResponseModel> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_activity)
        initView()
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

    override fun onResponse(response: Response<CommonResponseModel>) {
        CommonUtil.hideProgress()
        if (response.body() != null) {
            if (response.body()!!.success) {
                CommonUtil.showSnackbar(main_layout, response.body()!!.message)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            } else {
                CommonUtil.showSnackbar(main_layout, response.body()!!.message)
            }
        } else {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.something_wrong))
        }
    }

    override fun onFailure() {
        CommonUtil.hideProgress()
        CommonUtil.showSnackbar(main_layout, resources.getString(R.string.something_wrong))
    }

    @Inject
    lateinit var authManager: AuthManager

    lateinit var mPostApiResultCallback: PostAPIResultCallback<CommonResponseModel>

    private fun initView() {
        (application as MyApplication).getAppComponent()?.inject(this)
        mPostApiResultCallback = this

        toolbar_title.setText("Reset Password")

        back_button.setOnClickListener {
            finish()
        }
    }

    fun onSubmitClick(view: View) {
        if (CommonUtil.isConnectingToInternet(this)) {
            if (isValid()) {
                CommonUtil.showProgress(this)
                LoginApiMethods.resetPassword(
                    mPostApiResultCallback, intent.getStringExtra(Constants.KEY_EMAIL),
                    CommonUtil.fieldValue(et_new_password), CommonUtil.fieldValue(et_confirm_password),
                    intent.getStringExtra(Constants.KEY_CODE), CommonUtil.fieldValue(et_otp)
                )
            }
        } else {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
        }
    }

    private fun isValid(): Boolean {
        if (CommonUtil.checkEmptyString(et_otp)) {
            CommonUtil.showSnackbar(main_layout, "Please enter OTP")
            return false
        } else if (CommonUtil.checkEmptyString(et_new_password)) {
            CommonUtil.showSnackbar(main_layout, "Please enter new password")
            return false
        } else if (CommonUtil.checkEmptyString(et_confirm_password)) {
            CommonUtil.showSnackbar(main_layout, "Please confirm password")
            return false
        } else if (!CommonUtil.fieldValue(et_new_password).equals(CommonUtil.fieldValue(et_confirm_password))) {
            CommonUtil.showSnackbar(main_layout, "Password don't match")
            return false
        }

        return true
    }


}