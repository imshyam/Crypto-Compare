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

    private var  time: TextView = findViewById(R.id.highlighted_time_val)
    private var  price: TextView = findViewById(R.id.highlighted_price_val)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        time.text = e?.x.toString()
        price.text = e?.y.toString()
    }

    fun getXOffset(pos: Float): Int{
        return -width/2
    }

    fun getYOffset(pos: Float): Int{
        return -height
    }
}