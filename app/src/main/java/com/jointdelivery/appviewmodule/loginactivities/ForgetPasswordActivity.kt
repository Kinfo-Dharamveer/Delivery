package com.jointdelivery.appviewmodule.loginactivities

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.appviewmodule.model.ForgotPasswordModel
import com.jointdelivery.utilities.CommonUtil
import com.jointdelivery.utilities.Constants
import com.scyhealth.basemodule.interfaces.PostAPIResultCallback
import com.scyhealth.loginmodule.LoginApiMethods
import kotlinx.android.synthetic.main.activity_forget_password.*
import retrofit2.Response
import javax.inject.Inject

open class ForgetPasswordActivity : AppCompatActivity(), PostAPIResultCallback<ForgotPasswordModel> {
    override fun onResponse(response: Response<ForgotPasswordModel>) {

        CommonUtil.hideProgress()
        if (response.body() != null) {
            if (response.body()!!.success) {
                CommonUtil.showShortToast(this, response.body()!!.message)
                val intent = Intent(this, ResetPasswordActivity::class.java)
                intent.putExtra(Constants.KEY_EMAIL, CommonUtil.fieldValue(et_email))
                intent.putExtra(Constants.KEY_CODE, response.body()!!.code)
                startActivity(intent)
            } else {
                CommonUtil.showSnackbar(main_layout, response.body()!!.message)
            }
        } else {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.something_wrong))
        }
    }

    override fun onFailure() {
    }

    @Inject
    lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        (application as MyApplication).getAppComponent()?.inject(this)
    }

    fun isValid(): Boolean {
        if (CommonUtil.checkEmptyString(et_email)) {
            CommonUtil.showSnackbar(main_layout, "Please enter email")
            return false
        } else if (!CommonUtil.checkValidEmail(et_email)) {
            CommonUtil.showSnackbar(main_layout, "Please enter valid email")
            return false
        }

        return true
    }

    fun onResetPasswordClick(view: View) {
        if (CommonUtil.isConnectingToInternet(this)) {
            if (isValid()) {
                CommonUtil.showProgress(this)
                LoginApiMethods.forgotPassword(this, CommonUtil.fieldValue(et_email))
            }
        } else {
            CommonUtil.showSnackbar(main_layout, resources.getString(R.string.please_connect_internet))
        }
    }

    fun onBackToLoginClick(view: View) {
        finish()
    }


}
