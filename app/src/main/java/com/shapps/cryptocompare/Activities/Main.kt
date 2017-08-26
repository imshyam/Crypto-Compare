package com.shapps.cryptocompare.Activities

import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment;
import android.os.Bundle
import android.os.Handler
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
import com.shapps.cryptocompare.MainFragments.Charts
import com.shapps.cryptocompare.MainFragments.Dashboard
import com.shapps.cryptocompare.MainFragments.Notifications
import com.shapps.cryptocompare.R


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
                selectedFragment = Dashboard.newInstance(1)
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

        navigation.selectedItemId = R.id.navigation_dashboard
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        swiperefresh.setOnRefreshListener({
            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")
            updateOperation();
        })


        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_content_fragment, Dashboard.newInstance(1))
        transaction.commit()
    }

    private fun updateOperation() {
        Handler().postDelayed({
            swiperefresh.isRefreshing = false
        }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.action_settings -> {
                val settingsAct = Intent(applicationContext, Settings::class.java)
                startActivity(settingsAct)
                return true
            }
            R.id.menu_refresh -> {
                Log.i(LOG_TAG, "Refresh menu item selected");
                swiperefresh.isRefreshing = true
                updateOperation();
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onListFragmentInteraction(cryptoCurrency: String, currency: String, exchangeId: String, exchangeName: String) {
        var detailsIntent = Intent(this, Details::class.java)
        detailsIntent.putExtra("CRYPTO_CURR", cryptoCurrency)
        detailsIntent.putExtra("CURR", currency)
        detailsIntent.putExtra("EX_ID", exchangeId)
        detailsIntent.putExtra("EX_NAME", exchangeName)
        startActivity(detailsIntent)
    }

    override fun onFragmentInteraction(uri: Uri) {
        Log.e("Item", "URI" + uri)
    }

    override fun onListFragmentInteraction(item: NotificationContent.NotificationItem) {
        Log.e("Item", "val : " + item)
    }

}