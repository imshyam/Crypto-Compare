package com.shapps.cryptocompare

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
import com.shapps.cryptocompare.Model.LiveDataContent
import android.view.MenuItem
import com.shapps.cryptocompare.Model.NotificationContent


class MainActivity : AppCompatActivity(), DashboardFragment.OnListFragmentInteractionListener,
        NotificationsFragment.OnListFragmentInteractionListener, ChartsFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val selectedFragment: Fragment
        when (item.itemId) {
            R.id.navigation_charts -> {
                selectedFragment = ChartsFragment.newInstance("Apple", "Book")
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_content_fragment, selectedFragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                selectedFragment = DashboardFragment.newInstance(1)
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_content_fragment, selectedFragment)
                transaction.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                selectedFragment = NotificationsFragment.newInstance(10)
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

        val navigation = findViewById<View>(R.id.navigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_content_fragment, ChartsFragment.newInstance("Apple", "Book"))
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.action_settings-> {
                val settingsAct = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(settingsAct)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onListFragmentInteraction(item: LiveDataContent.LiveData) {
        Log.e("Item", "val : " + item)
    }

    override fun onFragmentInteraction(uri: Uri) {
        Log.e("Item", "URI" + uri)
    }

    override fun onListFragmentInteraction(item: NotificationContent.NotificationItem) {
        Log.e("Item", "val : " + item)
    }

}