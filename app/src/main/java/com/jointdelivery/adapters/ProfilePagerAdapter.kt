package com.jointdelivery.adapters


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jointdelivery.fragments.pagerFragments.VehicleInfoFragment
import com.jointdelivery.fragments.pagerFragments.YourInfoFragment


class ProfilePagerAdapter internal constructor(fm: FragmentManager, var profileDetail: HashMap<String, String>) :
    FragmentPagerAdapter(fm) {

    private val COUNT = 2

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        val bundle = Bundle()
        bundle.putSerializable("profileDetail", profileDetail)

        when (position) {

            0 -> fragment = YourInfoFragment()
            1 -> fragment = VehicleInfoFragment()

        }
        fragment!!.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "Your Info"
        } else if (position == 1) {
            title = "Vehicle Info"
        }
        return title
    }

}