package com.jointdelivery.fragments.homeFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.MapFragment
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.adapters.DriversListAdapter
import com.jointdelivery.appviewmodule.mapactivities.OrderDetailActivity
import com.jointdelivery.appviewmodule.mapactivities.YourRoutsMapActivity
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.utilities.onRecyclerViewClickListener
import kotlinx.android.synthetic.main.fragment_home_layout.view.*
import kotlinx.android.synthetic.main.tool_bar_layout.*
import javax.inject.Inject

open class HomeFragment : Fragment(), onRecyclerViewClickListener {

    @Inject
    lateinit var authManager: AuthManager

    lateinit var driverlistAdapter: DriversListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun initView(view: View) {
        (activity!!.application as MyApplication).getAppComponent()?.inject(this)
        view.rv_home_list.layoutManager = LinearLayoutManager(activity)
        driverlistAdapter = DriversListAdapter(
            activity!!, this
        )
        view.rv_home_list.adapter = driverlistAdapter
    }

    override fun onListItemClicks(position: Int) {
        val intent = Intent(activity, OrderDetailActivity::class.java)
        startActivity(intent)
    }
}