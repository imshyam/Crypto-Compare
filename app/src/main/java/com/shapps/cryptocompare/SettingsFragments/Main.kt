package com.shapps.cryptocompare.SettingsFragments

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import com.shapps.cryptocompare.SettingsFragments.ManageExchanges.Bitcoin
import com.shapps.cryptocompare.SettingsFragments.ManageExchanges.Ethereum
import com.shapps.cryptocompare.R
import com.shapps.cryptocompare.Activities.Settings

/**
 * Created by shyam on 26/8/17.
 */
class Main : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_settings)

        findPreference("pref_key_storage_bitcoin_exchanges").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            fragmentManager.beginTransaction().replace(android.R.id.content, Bitcoin()).addToBackStack(Bitcoin::class.java.simpleName).commit()
            true
        }

        findPreference("pref_key_storage_ethereum_exchanges").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            fragmentManager.beginTransaction().replace(android.R.id.content, Ethereum()).addToBackStack(Ethereum::class.java.simpleName).commit()
            true
        }


        val settingsAct = activity as Settings
        settingsAct.title = "Settings"
        settingsAct.flag = true

        Settings.bindPreferenceSummaryToValue(findPreference("pref_key_storage_min_max_period"))
        Settings.bindPreferenceSummaryToValue(findPreference("pref_key_storage_graph_type"))
        Settings.bindPreferenceSummaryToValue(findPreference("pref_key_storage_alarm_tone"))

    }

}