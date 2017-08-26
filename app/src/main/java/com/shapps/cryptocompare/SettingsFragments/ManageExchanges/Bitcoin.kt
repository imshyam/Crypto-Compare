package com.shapps.cryptocompare.SettingsFragments.ManageExchanges

import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.MenuItem
import com.shapps.cryptocompare.R
import com.shapps.cryptocompare.Activities.SettingsActivity

/**
 * Created by shyam on 26/8/17.
 */

class Bitcoin : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_bitcoin_exchanges)

        val settingsAct = activity as SettingsActivity
        settingsAct.title = "Bitcoin Exchanges"
        settingsAct.flag = false

        setHasOptionsMenu(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            fragmentManager.beginTransaction().replace(android.R.id.content, SettingsActivity.Main()).commit()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}