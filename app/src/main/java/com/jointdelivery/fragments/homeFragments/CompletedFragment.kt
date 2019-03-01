package com.jointdelivery.fragments.homeFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jointdelivery.MyApplication
import com.jointdelivery.R
import com.jointdelivery.adapters.CompletedOrderAdapter
import com.jointdelivery.appviewmodule.mapactivities.CompletedOrderDetailActivity
import com.jointdelivery.appviewmodule.mapactivities.OrderDetailActivity
import com.jointdelivery.auth.AuthManager
import com.jointdelivery.utilities.onRecyclerViewClickListener
import kotlinx.android.synthetic.main.fragment_completed_layout.view.*
import javax.inject.Inject

open class CompletedFragment : Fragment(), onRecyclerViewClickListener {
    override fun onListItemClicks(position: Int) {
        val intent = Intent(activity, CompletedOrderDetailActivity::class.java)
        startActivity(intent)
    }

    @Inject
    lateinit var authManager: AuthManager

    lateinit var completedOrderAdapter: CompletedOrderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_completed_layout, container, false)

        initView(view)

        return view
    }

    fun initView(view: View) {
        (activity!!.application as MyApplication).getAppComponent()?.inject(this)

        view.rv_completed_list.layoutManager = LinearLayoutManager(activity)
        completedOrderAdapter = CompletedOrderAdapter(
            activity!!
            , this
        )
        view.rv_completed_list.adapter = completedOrderAdapter
    }

}