package com.jointdelivery.appviewmodule

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jointdelivery.R
import kotlinx.android.synthetic.main.tool_bar_layout.*

open class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_layout)
        back_button.visibility = View.VISIBLE
    }

}