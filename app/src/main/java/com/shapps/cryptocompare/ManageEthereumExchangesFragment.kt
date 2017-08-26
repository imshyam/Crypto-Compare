package com.shapps.cryptocompare

import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.MenuItem

/**
 * Created by shyam on 26/8/17.
 */
class ManageEthereumExchangesFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_bitcoin_exchanges)

        val settingsAct = activity as SettingsActivity
        settingsAct.title = "Ethereum Exchanges"
        settingsAct.flag = false

        setHasOptionsMenu(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            fragmentManager.beginTransaction().replace(android.R.id.content, SettingsActivity.SettingsFragment()).commit()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}