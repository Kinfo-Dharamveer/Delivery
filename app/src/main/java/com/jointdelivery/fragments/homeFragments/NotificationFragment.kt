package com.jointdelivery.fragments.homeFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.adapters.MessagesAdapter
import com.jointdelivery.adapters.NotificationAdapter
import com.jointdelivery.auth.AuthManager
import kotlinx.android.synthetic.main.fragment_messages_fragment.view.*
import javax.inject.Inject

open class NotificationFragment:Fragment(){

    @Inject
    lateinit var authManager: AuthManager

    lateinit var messagesAdapter: NotificationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_messages_fragment, container, false)

        initView(view)

        return view
    }

    fun initView(view: View) {
        (activity!!.application as MyApplication).getAppComponent()?.inject(this)

        view.rv_messages_list.layoutManager = LinearLayoutManager(activity)
        messagesAdapter = NotificationAdapter(
            activity!!
        )
        view.rv_messages_list.adapter = messagesAdapter
    }
}