package com.example.android2023ej.classes

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(context: Context, dividerResId: Int) : RecyclerView.ItemDecoration() {
    private val divider: Drawable?

    // Initialize the divider drawable
    init {
        divider = ContextCompat.getDrawable(context, dividerResId)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + divider?.intrinsicHeight!!

            // Set the bounds of the divider
            divider?.setBounds(left, top, right, bottom)
            // Draw the divider
            divider?.draw(canvas)
        }
    }
}
