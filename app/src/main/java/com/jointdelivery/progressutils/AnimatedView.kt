package com.jointdelivery.progressutils

import android.content.Context
import android.view.View

class AnimatedView(context: Context) : View(context) {

    var target: Int = 0

    var xFactor: Float
        get() = x / target
        set(xFactor) {
            x = target * xFactor
        }
}