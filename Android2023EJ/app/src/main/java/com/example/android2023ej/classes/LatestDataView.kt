package com.example.android2023ej.classes

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import com.example.android2023ej.R


class LatestDataView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
val maxRows = 5
    init {
        // Set LinearLayout orientation ti vertucak
        this.orientation = VERTICAL

        // Create a temporary TextView to calculate the height of a single TextView
        var someTextView : TextView = TextView(context)

        // Measure the size of the TextView if it were on the screen
        someTextView.measure(0,0)

        // Height of a single TextView
        var rowHeight = someTextView.measuredHeight

        // Measure the height of the LinearLayout
        this.measure(0,0)

        // Calculate additional height for the LinearLayout
        var additionalHeight = this.measuredHeight


        // Set the minimum height of the LinearLayout to fit five TextViews,
        this.minimumHeight = rowHeight * maxRows + additionalHeight

    }

    fun addData(message: String) {

        // Remove the oldest TextViews when theres too many
        while(this.childCount >= maxRows) {
            this.removeViewAt(0)
        }


        // Create a new TextView and animations
        var newTextView : TextView = TextView(context) as TextView
        val fadeAnimation : Animation = AnimationUtils.loadAnimation(context, R.anim.customfade)
        newTextView.startAnimation(fadeAnimation)
        newTextView.setText(message)
        newTextView.setTextColor(Color.rgb(3,218,197))
        newTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        this.addView(newTextView)
    }

    }


