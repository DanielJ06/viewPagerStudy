package com.tab.storiesclone.ui.main

import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

class CubeTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        val deltaY = 0.5F

        view.pivotX = if (position < 0F) view.width.toFloat() else 0F
        view.pivotY = view.height * deltaY
        view.rotationY = 35F * position

        val alphaFactor = max(0.0f, 1 - abs(position))
        view.alpha = alphaFactor
    }

}