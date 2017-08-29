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


/**
 * Created by shyam on 26/8/17.
 */

class Bitcoin : PreferenceFragment() {

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
            var jsonObj = JSONObject(response);
            var allBitcoinExchanges =  JSONObject(jsonObj.getString("Bitcoin"))
            val keys = allBitcoinExchanges.keys()

            while (keys.hasNext()) {
                val key = keys.next() as String
                var currencywiseB =  JSONArray(allBitcoinExchanges.get(key).toString())
//                Log.d("CurrencyWise", currencywiseB.toString())
                (0 until currencywiseB.length())
                        .map { JSONObject(currencywiseB.get(it).toString()) }
                        .forEach {
                            Log.d(key, it.getString("name"))
                            var switchPref = SwitchPreference(contextThemeWrapper)
                            switchPref.title = it.getString("name")
                            switchPref.key = "pref_key_storage_bitcoin_exchanges_" + key + "_" + it.getString("name")
                            switchPref.summary = key
                            preferenceScreen.addPreference(switchPref)
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