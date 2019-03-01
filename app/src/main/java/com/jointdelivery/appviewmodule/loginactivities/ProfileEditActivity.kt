package com.jointdelivery.appviewmodule.loginactivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.jointdelivery.R
import com.jointdelivery.adapters.ProfilePagerAdapter;
import kotlinx.android.synthetic.main.edit_profile_layout.*
import kotlinx.android.synthetic.main.tool_bar_layout.*


open class ProfileEditActivity : AppCompatActivity() {
    var profileDetail: HashMap<String, String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.edit_profile_layout)
        toolbar_title.setText(resources.getString(R.string.profile_edit))
        back_button.setOnClickListener { finish() }
        profileDetail = intent?.getSerializableExtra("profileDetail") as HashMap<String, String>
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        if (viewPager != null) {
            val adapter = ProfilePagerAdapter(supportFragmentManager, profileDetail);
            viewPager.adapter = adapter
            tabs.setupWithViewPager(viewPager);
        }
    }
}