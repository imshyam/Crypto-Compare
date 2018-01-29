package com.shapps.cryptocompare.CustomViews

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.shapps.cryptocompare.R

/**
 * Created by shyam on 29/1/18.
 */
class CustomMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {

    private lateinit var  textView: TextView

    init {
        textView = findViewById(R.id.highlighted_val)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        textView.text = e.toString()
    }

    fun getXOffset(pos: Float): Int{
        return -width/2
    }

    fun getYOffset(pos: Float): Int{
        return -height
    }
}