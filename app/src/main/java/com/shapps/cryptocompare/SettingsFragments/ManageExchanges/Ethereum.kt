package com.shapps.cryptocompare.SettingsFragments.ManageExchanges

import android.app.ProgressDialog
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.util.Log
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.shapps.cryptocompare.R
import com.shapps.cryptocompare.Activities.Settings
import com.shapps.cryptocompare.Model.ExchangeDetailsDbHelper
import com.shapps.cryptocompare.Model.ExchangeDetailsSchema
import com.shapps.cryptocompare.Networking.AppController
import com.shapps.cryptocompare.Networking.DetailURLs
import com.shapps.cryptocompare.SettingsFragments.Main
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by shyam on 26/8/17.
 */
class Ethereum : PreferenceFragment() {
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

        val mDbHelper = ExchangeDetailsDbHelper(activity)

        val db = mDbHelper.readableDatabase

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        val projection = arrayOf(
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_ID,
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_EX_NAME,
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_CURRENCY)

// Filter results WHERE "title" = 'My Title'
        val selection = ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_CRYPTO_CURRENCY + " = ?"
        val selectionArgs = arrayOf("Ethereum")

// How you want the results sorted in the resulting Cursor
        val sortOrder = ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_ID + " ASC"

        val cursor = db.query(
                ExchangeDetailsSchema.ExchangesDetailsEntry.TABLE_NAME, // The table to query
                projection, // The columns to return
                selection, // The columns for the WHERE clause
                selectionArgs, // don't group the rows
                null, null, // don't filter by row groups
                sortOrder                                 // The sort order
        )
        while (cursor.moveToNext()) {
            var ex_id = cursor.getInt(0)
            var ex_name = cursor.getString(1)
            var currency = cursor.getString(2)
            var switchPref = SwitchPreference(contextThemeWrapper)
            switchPref.title = ex_name
            switchPref.key = "pref_key_storage_bitcoin_exchanges_" + ex_id
            switchPref.summary = currency
            preferenceScreen.addPreference(switchPref)

        }
        cursor.close()
        val settingsAct = activity as Settings
        settingsAct.title = "Ethereum Exchanges"
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