package com.deepa.weather.main.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class VerticalSpaceDecorator(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration(){
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView , state: RecyclerView.State) {
        outRect.bottom = verticalSpaceHeight
    }
}