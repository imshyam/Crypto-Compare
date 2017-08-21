package com.shapps.cryptocompare

import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {


    private var cryptoCurrency: String? = null
    private var currency: String? = null
    private var siteId: Int? = null
    private var siteName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        setupActionBar()

        var upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)

        collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsing_toolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);


        cryptoCurrency = intent.getStringExtra("CRYPTO_CURR")
        currency = intent.getStringExtra("CURR")
        siteId = intent.getStringExtra("EX_ID").toInt()
        siteName = intent.getStringExtra("EX_NAME")
        var entries = ArrayList<Entry>()
        for (i in 1..25) {
            entries.add(Entry(i.toFloat(), i.toFloat()))
        }
        var lds = LineDataSet(entries, "India")
        lds.color = Color.parseColor("#003838")
        lds.valueTextColor = Color.parseColor("#bbbbbb")

        var lineData = LineData(lds)

        exchange_chart.data = lineData
        exchange_chart.invalidate()
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.finish()
        }
        return true
    }

}
