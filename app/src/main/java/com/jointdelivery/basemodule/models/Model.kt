package com.jointdelivery.basemodule.models

import com.jointdelivery.R

enum class Model private constructor(val titleResId: Int, val layoutResId: Int) {
    RED(R.string.profile_edit, R.layout.fragment_profile_edit_layout),
    BLUE(R.string.vehicle_info, R.layout.fragment_vehicle_info_layout)

}