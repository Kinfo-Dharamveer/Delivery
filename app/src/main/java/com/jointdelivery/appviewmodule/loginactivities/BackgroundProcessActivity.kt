package com.jointdelivery.appviewmodule.loginactivities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.basemodule.activities.HomeActivity
import com.jointdelivery.utilities.CommonUtil
import kotlinx.android.synthetic.main.activity_background_process.*
import kotlinx.android.synthetic.main.tool_bar_layout.*
import javax.inject.Inject
import android.view.WindowManager
import android.app.AlertDialog


open class BackgroundProcessActivity : AppCompatActivity() {
    @Inject
    lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background_process)
        toolbar_title.text = resources.getString(R.string.title_background_process)
        back_button.setOnClickListener { finishAffinity() }
        (application as MyApplication).getAppComponent()?.inject(this)
    }


    private fun showDialog() {
        val adb = AlertDialog.Builder(this)
        var dialogs = Dialog(this)
        dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogs.setCancelable(false)
        dialogs.setContentView(R.layout.dialog_response)
        dialogs.window.setBackgroundDrawableResource(android.R.color.transparent)
        // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

        val body = dialogs.findViewById(R.id.textView2_content) as TextView
        val title = dialogs.findViewById(R.id.textView_title) as TextView
        title.setText(resources.getString(R.string.you_made_it))
        back_button.visibility = View.VISIBLE
        back_button.setOnClickListener {
            onBackPressed()}
        body.setText(resources.getString(R.string.screening_process_done))
        val yesBtn = dialogs.findViewById(com.jointdelivery.R.id.button_ok) as Button
        yesBtn.setOnClickListener {
            dialogs.dismiss()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogs.getWindow()!!.getAttributes())
        lp.width = WindowManager.LayoutParams.MATCH_PARENT

        dialogs.show()
        dialogs.getWindow()!!.setAttributes(lp)
    }

    fun continuebtn(view: View) {
        if (checkbox_accept.isChecked) {
            authManager.setBackgroundProcessDone(true)
            showDialog()
        } else {
            CommonUtil.showSnackbar(main_layout, "Please check to continue")
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
    }
}