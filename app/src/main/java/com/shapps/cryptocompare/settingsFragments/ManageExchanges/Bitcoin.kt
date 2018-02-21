package com.shapps.cryptocompare.settingsFragments.ManageExchanges

import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.MenuItem
import com.shapps.cryptocompare.R
import com.shapps.cryptocompare.activities.Settings
import com.shapps.cryptocompare.settingsFragments.Main
import android.preference.SwitchPreference
import android.util.TypedValue
import android.view.ContextThemeWrapper
import com.shapps.cryptocompare.model.ExchangeDetailsDbHelper
import com.shapps.cryptocompare.model.ExchangeDetailsSchema
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.content.ContentValues
import android.util.Log
import com.shapps.cryptocompare.model.ExchangeDetailsSchema.ExchangesDetailsEntry.*
import java.util.regex.Pattern

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
        val selectionArgs = arrayOf("Bitcoin")

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
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        // Use instance field for listener
// It will not be gc'd as long as this instance is kept referenced
        var listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->

            if(key.contains("exchanges")) {
                val active = prefs.getBoolean(key, false)
                var updateTo = "0"

                if (active)
                    updateTo = "1"


                val values = ContentValues()
                values.put(COLUMN_NAME_ACTIVE, updateTo)

                val p = Pattern.compile("\\d+")
                val m = p.matcher(key)
                var ex_id = "1"
                while (m.find()) {
                    ex_id = m.group()
                }


// Which row to update, based on the title
                val selection = COLUMN_NAME_ID + " = ?"
                val selectionArgs = arrayOf(ex_id)

                val count = db.update(
                        TABLE_NAME,
                        values,
                        selection,
                        selectionArgs)

                Log.d("Updated Rows", count.toString())
            }

        }

        prefs.registerOnSharedPreferenceChangeListener(listener)
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