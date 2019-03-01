package com.jointdelivery.appviewmodule.loginactivities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.basemodule.activities.BaseActivity
import com.jointdelivery.basemodule.activities.HomeActivity
import com.jointdelivery.appviewmodule.model.LoginModel
import com.jointdelivery.utilities.CommonUtil
import com.jointdelivery.utilities.Constants


import com.scyhealth.loginmodule.LoginApiMethods

import com.scyhealth.basemodule.interfaces.PostAPIResultCallback

import retrofit2.Response
import javax.inject.Inject

class SplashScreenActivity : BaseActivity(), PostAPIResultCallback<LoginModel> {

    @Inject
    lateinit var authManager: AuthManager

    lateinit var mHandler: Handler
    private val SPLASH_DELAY: Long = 2000

    lateinit var mPostApiResultCallabacl: PostAPIResultCallback<LoginModel>

    internal val mRunnable: Runnable = Runnable {
        setLoginIntent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        initView()
    }

    private fun initView() {
        (application as MyApplication).getAppComponent()?.inject(this)

        mPostApiResultCallabacl = this

        mHandler = Handler()

        if (authManager.getAccessToken().equals("")) {
            mHandler.postDelayed(mRunnable, SPLASH_DELAY)
        } else {
            if (CommonUtil.isConnectingToInternet(this)) {
                getRefreshToken()
            } else {
                if (authManager.getAccessToken().equals("")) {
                    setLoginIntent()
                } else {
                    if (authManager.getBackgroundProcessDone()) {
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(applicationContext, BackgroundProcessActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    fun getRefreshToken() {
        CommonUtil.showProgress(this)
        LoginApiMethods.refreshToken(mPostApiResultCallabacl, authManager.getRefreshToken(), Constants.REFRESH_TOKEN)
    }

    public override fun onDestroy() {
        mHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }


    override fun onResponse(response: Response<LoginModel>) {
        CommonUtil.hideProgress()
        if (true) {
            if (response.errorBody() != null) run {
                setLoginIntent()
            } else {
                if (response.body() != null) {
                    CommonUtil.saveLoginSession(authManager, response)

                    val intent: Intent

                    if (authManager.getBackgroundProcessDone()) {
                        intent = Intent(applicationContext, HomeActivity::class.java)
                    } else {
                        intent = Intent(applicationContext, BackgroundProcessActivity::class.java)
                    }
                    startActivity(intent)
                    finishAffinity()
                } else {
                    CommonUtil.showShortToast(this, resources.getString(R.string.something_wrong))
                    setLoginIntent()
                }
            }
        }
    }

    override fun onFailure() {
        CommonUtil.hideProgress()
        CommonUtil.showShortToast(this, resources.getString(R.string.something_wrong))
        setLoginIntent()
    }

    fun setLoginIntent() {

        authManager.clearPrefernces()

        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}