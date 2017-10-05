package com.shapps.cryptocompare.SettingsFragments.ManageExchanges

import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.MenuItem
import com.shapps.cryptocompare.R
import com.shapps.cryptocompare.Activities.Settings
import com.shapps.cryptocompare.SettingsFragments.Main
import com.shapps.cryptocompare.Networking.AppController
import com.android.volley.VolleyLog
import android.app.ProgressDialog
import android.preference.SwitchPreference
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.shapps.cryptocompare.Networking.DetailURLs
import org.json.JSONArray
import org.json.JSONObject
import android.util.TypedValue
import android.view.ContextThemeWrapper
import com.shapps.cryptocompare.Model.LiveDataContent


/**
 * Created by shyam on 26/8/17.
 */

class Bitcoin : PreferenceFragment() {
    /**
     * Shared Preferences File Name
     */
    private val PREF_FILE = "ExchangesList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var prefBitcoinsScreen = preferenceManager.createPreferenceScreen(activity)
        preferenceScreen = prefBitcoinsScreen

        val themeTypedValue = TypedValue()
        activity.theme.resolveAttribute(R.style.AppTheme, themeTypedValue, true)
        val contextThemeWrapper = ContextThemeWrapper(activity, themeTypedValue.resourceId)

        var url = DetailURLs.URL_EXCHANGES

        val pDialog = ProgressDialog(activity)
        pDialog.setMessage("Loading...")
        pDialog.show()

        val strReq = StringRequest(Request.Method.GET,
                url, Response.Listener { response ->
            var jsonArr = JSONArray(response)

        (0 until jsonArr.length())
                .map { JSONObject(jsonArr.get(it).toString()) }
                .forEach {
                    if (it.getString("crypto_currency").equals("Bitcoin")) {
                        var currency = it.getString("currency")
                        Log.d("Exchange", it.getString("name"))
                        var switchPref = SwitchPreference(contextThemeWrapper)
                        switchPref.title = it.getString("name")
                        switchPref.key = "pref_key_storage_bitcoin_exchanges_" + it.getString("id")
                        switchPref.summary = currency
                        preferenceScreen.addPreference(switchPref)

                        var sharedPref = activity.getSharedPreferences(PREF_FILE, 0)
                        if (!sharedPref.contains(it.getInt("id").toString())){
                            val editor = sharedPref.edit()
                            editor.putString(it.getInt("id").toString(), it.getString("name"))
                            editor.commit()
                        }
                    }
                }
            pDialog.hide()
        }, Response.ErrorListener { error ->
            VolleyLog.d("TAG ", "Error: " + error.message)
            pDialog.hide()
        })

        // Adding request to request queue
        AppController.instance?.addToRequestQueue(strReq, "APPLE", activity)


//        addPreferencesFromResource(R.xml.pref_bitcoin_exchanges)

        val settingsAct = activity as Settings
        settingsAct.title = "Bitcoin Exchanges"
        settingsAct.flag = false

        setHasOptionsMenu(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            fragmentManager.beginTransaction().replace(android.R.id.content, Main()).commit()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}