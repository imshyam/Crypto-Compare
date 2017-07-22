package com.shapps.cryptocompare

import android.net.Uri
import android.support.v4.app.Fragment;
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.shapps.cryptocompare.Model.LiveDataContent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DashboardFragment.OnListFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener, ChartsFragment.OnFragmentInteractionListener {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment
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
                selectedFragment = SettingsFragment.newInstance("Cat", "Dog")
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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_content_fragment, ChartsFragment.newInstance("Apple", "Book"))
        transaction.commit()
    }

    override fun onListFragmentInteraction(item: LiveDataContent.LiveData) {
        Log.e("Item", "val : " + item)
    }

    override fun onFragmentInteraction(uri: Uri) {
        Log.e("Item", "URI" + uri)
    }

}
