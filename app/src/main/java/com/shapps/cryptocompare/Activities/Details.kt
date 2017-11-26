package com.shapps.cryptocompare.Activities

import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.shapps.cryptocompare.Networking.History
import com.shapps.cryptocompare.R
import kotlinx.android.synthetic.main.activity_details.*

class Details : AppCompatActivity() {


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
        siteId = intent.getStringExtra("EX_ID")
        siteName = intent.getStringExtra("EX_NAME")
        buy = intent.getStringExtra("BUY")
        sell = intent.getStringExtra("SELL")
        buyLow = intent.getStringExtra("BUY_LOW")
        buyHigh = intent.getStringExtra("BUY_HIGH")
        sellLow = intent.getStringExtra("SELL_LOW")
        sellHigh = intent.getStringExtra("SELL_HIGH")

        var term = "hours=1"

        History.draw(siteId, siteName, term, this, exchange_chart)

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
