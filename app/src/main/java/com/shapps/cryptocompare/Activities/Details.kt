package com.shapps.cryptocompare.Activities

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.shapps.cryptocompare.Networking.History
import com.shapps.cryptocompare.R
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.chart_header.*
import java.text.SimpleDateFormat
import java.util.*

class Details : AppCompatActivity(), View.OnClickListener {

    private var cryptoCurrency: String? = null
    private var currency: String? = null
    private var siteId: String = ""
    private var siteName: String = ""
    private var buy: String = ""
    private var sell: String = ""
    private var buyLow: String = ""
    private var buyHigh: String = ""
    private var sellLow: String = ""
    private var sellHigh: String = ""
    private var volume: String = ""

    private lateinit var view_main: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        view_main = findViewById(android.R.id.content)

        setSupportActionBar(toolbar)
        setupActionBar()

        val upArrow: Drawable = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)

        collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar)
        collapsing_toolbar.setExpandedTitleTextAppearance(R.style.expandedappbar)

        period_1_hour.setOnClickListener(this)
        period_1_day.setOnClickListener(this)
        period_1_week.setOnClickListener(this)
        period_1_month.setOnClickListener(this)
        period_all.setOnClickListener(this)


        cryptoCurrency = intent.getStringExtra("CRYPTO_CURR")
        currency = intent.getStringExtra("CURR")
        siteId = intent.getStringExtra("EX_ID")
        siteName = intent.getStringExtra("EX_NAME")
        buy = intent.getStringExtra("BUY")
        sell = intent.getStringExtra("SELL")
        buyLow = intent.getStringExtra("BUY_LOW")
        buyHigh = intent.getStringExtra("BUY_HIGH")
        sellLow = intent.getStringExtra("SELL_LOW")
        sellHigh = intent.getStringExtra("SELL_HIGH")
        volume = intent.getStringExtra("VOL")

        buy_value.text = buy
        sell_value.text = sell
        buy_low.text = buyLow
        buy_high.text = buyHigh
        sell_low.text = sellLow
        sell_high.text = sellHigh
        vol_text_view.text = volume

        // set value
        val dateFormatGmt = SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
        dateFormatGmt.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormatLocal = SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
        var currDateTime = dateFormatLocal.parse(dateFormatGmt.format(Date()))
        time_selected.text = currDateTime.toString()
        price_selected.text = buy

        val term = "period=hour"

        exchange_chart.setDrawMarkers(false)

        exchange_chart.setOnChartValueSelectedListener(object: OnChartValueSelectedListener{
            override fun onNothingSelected() {
                time_selected.text = currDateTime.toString()
                price_selected.text = buy
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                time_selected.text = exchange_chart.xAxis.valueFormatter.getFormattedValue(e!!.x, exchange_chart.xAxis)
                price_selected.text = h!!.y.toString()
            }

        })

        History.draw(siteId, siteName, term, this, view_main, "", "", false, 0f, 0f)

    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.period_1_hour -> {
                val x = v as Button
                updateStyle(x)
                History.draw(siteId, siteName, "period=hour", this, view_main, "", "", false, 0f, 0f)
            }
            R.id.period_1_day -> {
                val x = v as Button
                updateStyle(x)
                History.draw(siteId, siteName, "period=day", this, view_main, "", "", false, 0f, 0f)
            }
            R.id.period_1_week -> {
                val x = v as Button
                updateStyle(x)
                History.draw(siteId, siteName, "period=week", this, view_main, "", "", false, 0f, 0f)
            }
            R.id.period_1_month -> {
                val x = v as Button
                updateStyle(x)
                History.draw(siteId, siteName, "period=month", this, view_main, "", "", false, 0f, 0f)
            }
            R.id.period_all -> {
                val x = v as Button
                updateStyle(x)
                History.draw(siteId, siteName, "period=all", this, view_main, "", "", false, 0f, 0f)
            }
        }
    }

    private fun updateStyle(x: Button) {
        removeCurrentStyle()
        x.background = ContextCompat.getDrawable(this, R.drawable.tag_currency_rounded)
        x.setTextColor(Color.WHITE)
    }
    private fun removeCurrentStyle() {
        period_1_hour.background = ContextCompat.getDrawable(this, R.drawable.time_period_rounded)
        period_1_hour.setTextColor(Color.BLACK)
        period_1_day.background = ContextCompat.getDrawable(this, R.drawable.time_period_rounded)
        period_1_day.setTextColor(Color.BLACK)
        period_1_week.background = ContextCompat.getDrawable(this, R.drawable.time_period_rounded)
        period_1_week.setTextColor(Color.BLACK)
        period_1_month.background = ContextCompat.getDrawable(this, R.drawable.time_period_rounded)
        period_1_month.setTextColor(Color.BLACK)
        period_all.background = ContextCompat.getDrawable(this, R.drawable.time_period_rounded)
        period_all.setTextColor(Color.BLACK)
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
