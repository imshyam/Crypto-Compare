package com.shapps.cryptocompare.Activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment;
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.MenuItem
import com.shapps.cryptocompare.Model.NotificationContent
import kotlinx.android.synthetic.main.activity_main.*
import com.github.mikephil.charting.charts.Chart.LOG_TAG
import com.shapps.cryptocompare.Constants.Exchanges
import com.shapps.cryptocompare.Fragments.Charts
import com.shapps.cryptocompare.Fragments.Dashboard
import com.shapps.cryptocompare.Fragments.Notifications
import com.shapps.cryptocompare.R
import java.io.IOException
import java.nio.charset.Charset
import com.shapps.cryptocompare.Model.ExchangeDetailsDbHelper
import com.shapps.cryptocompare.Model.ExchangeDetailsSchema.ExchangesDetailsEntry.*


class Main : AppCompatActivity(), Dashboard.OnListFragmentInteractionListener,
        Notifications.OnListFragmentInteractionListener, Charts.OnFragmentInteractionListener {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment
        when (item.itemId) {
            R.id.navigation_charts -> {
                selectedFragment = Charts.newInstance("Apple", "Book")
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_content_fragment, selectedFragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                selectedFragment = Dashboard.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_content_fragment, selectedFragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                selectedFragment = Notifications.newInstance(10)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_content_fragment, selectedFragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        var filename = "exchanges.json"

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        val savedFileName = sharedPref.getString(getString(R.string.saved_file_name), "NO_DATA")
        if(savedFileName == filename){
            Log.d("Already Loaded", filename)
        } else{
            Log.d("Loading", filename)
            var json = loadJSONFromAsset(filename)
            var success = Exchanges.saveData(json, this)
            if(success){
                val editor = sharedPref.edit()
                editor.putString(getString(R.string.saved_file_name), filename)
                editor.commit()
            }
        }

        navigation.selectedItemId = R.id.navigation_dashboard
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        swiperefresh.setOnRefreshListener({
            swiperefresh.isRefreshing = false
            updateOperation()
        })


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_content_fragment, Dashboard.newInstance())
        transaction.commit()
    }

    private fun updateOperation() {
        var refreshFragment = Dashboard.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_content_fragment, refreshFragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                val settingsAct = Intent(applicationContext, Settings::class.java)
                startActivity(settingsAct)
                true
            }
            R.id.menu_refresh -> {
                Log.i(LOG_TAG, "Refresh menu item selected");
                updateOperation();
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        updateOperation()
        super.onResume()
    }

    override fun onListFragmentInteraction(cryptoCurrency: String, currency: String, exchangeId: String, exchangeName: String, buy: String, sell: String,
                                           buyLow: String, buyHigh: String, sellLow: String, sellHigh: String, vol: String) {
        var detailsIntent = Intent(this, Details::class.java)
        detailsIntent.putExtra("CRYPTO_CURR", cryptoCurrency)
        detailsIntent.putExtra("CURR", currency)
        detailsIntent.putExtra("EX_ID", exchangeId)
        detailsIntent.putExtra("EX_NAME", exchangeName)
        detailsIntent.putExtra("BUY", buy)
        detailsIntent.putExtra("SELL", sell)
        detailsIntent.putExtra("BUY_LOW", buyLow)
        detailsIntent.putExtra("BUY_HIGH", buyHigh)
        detailsIntent.putExtra("SELL_LOW", sellLow)
        detailsIntent.putExtra("SELL_HIGH", sellHigh)
        detailsIntent.putExtra("VOL", vol)
        startActivity(detailsIntent)
    }

    override fun onFragmentInteraction(uri: Uri) {
        Log.e("Item", "URI" + uri)
    }

    override fun onListFragmentInteraction(item: NotificationContent.NotificationItem) {
        Log.e("Item", "val : " + item)
    }

    private fun loadJSONFromAsset(filename: String): String? {
        var json: String? = null
        try {
            val `is` = this.assets.open(filename)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }


}