package com.shapps.cryptocompare.activities

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
import com.shapps.cryptocompare.model.NotificationContent
import kotlinx.android.synthetic.main.activity_main.*
import com.github.mikephil.charting.charts.Chart.LOG_TAG
import com.shapps.cryptocompare.constants.Exchanges
import com.shapps.cryptocompare.fragments.Charts
import com.shapps.cryptocompare.fragments.Dashboard
import com.shapps.cryptocompare.fragments.Notifications
import com.shapps.cryptocompare.R
import java.io.IOException
import java.nio.charset.Charset


class Main : AppCompatActivity(), Dashboard.OnListFragmentInteractionListener,
        Notifications.OnListFragmentInteractionListener, Charts.OnFragmentInteractionListener {

    /** 0 = Charts Fragment
     *  1 = Dashboard
     *  2 = notification
     */
    private var position = 1

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment
        when (item.itemId) {
            R.id.navigation_charts -> {
                position = 0
                selectedFragment = Charts.newInstance("Apple", "Book")
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_content_fragment, selectedFragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                position = 1
                selectedFragment = Dashboard.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_content_fragment, selectedFragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                position = 2
                selectedFragment = Notifications.newInstance(10)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_content_fragment, selectedFragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //Save the fragment's instance
        outState.putInt("curChoice", position);
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val myToolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
//        setSupportActionBar(myToolbar)

        // Save Instance
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            position = savedInstanceState.getInt("curChoice", 1)
        }

        var filename = "exchanges_v2.json"

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

        when (position) {
            0 -> navigation.selectedItemId = R.id.navigation_charts
            1 -> navigation.selectedItemId = R.id.navigation_dashboard
            else -> navigation.selectedItemId = R.id.navigation_notifications
        }
    }

    private fun updateOperation() {
        when (position) {
            0 -> navigation.selectedItemId = R.id.navigation_charts
            1 -> navigation.selectedItemId = R.id.navigation_dashboard
            // Don't refresh notification
        }
    }

    // ToDo Create Refresh and settings
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.toolbar_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle item selection
//        return when (item.itemId) {
//            R.id.action_settings -> {
//                val settingsAct = Intent(applicationContext, Settings::class.java)
//                startActivity(settingsAct)
//                true
//            }
//            R.id.menu_refresh -> {
//                Log.i(LOG_TAG, "Refresh menu item selected");
//                updateOperation();
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onResume() {
        updateOperation()
        super.onResume()
        when (position) {
            0 -> navigation.selectedItemId = R.id.navigation_charts
            1 -> navigation.selectedItemId = R.id.navigation_dashboard
            else -> navigation.selectedItemId = R.id.navigation_notifications
        }
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