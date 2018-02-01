package com.shapps.cryptocompare.Constants

import android.util.Log
import com.shapps.cryptocompare.Activities.Main
import com.shapps.cryptocompare.Model.ExchangeDetailsDbHelper

import org.json.JSONArray
import org.json.JSONObject
import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import com.shapps.cryptocompare.Model.ExchangeDetailsSchema.ExchangesDetailsEntry.*


/**
 * Created by shyam on 28/10/17.
 */

class Exchanges {


    companion object {

        fun saveData(json: String?, context: Main?): Boolean {
            try {
                var jsonArr = JSONArray(json)
                val mDbHelper = ExchangeDetailsDbHelper(context)
                val db = mDbHelper.writableDatabase
                db.execSQL("delete from " + TABLE_NAME)
                (0 until jsonArr.length())
                        .map { JSONObject(jsonArr.get(it).toString()) }
                        .forEach {
                            // Create a new map of values, where column names are the keys
                            val values = ContentValues()
                            values.put(COLUMN_NAME_ID, it.getString("id"))
                            values.put(COLUMN_NAME_CRYPTO_CURRENCY, it.getString("crypto_currency"))
                            values.put(COLUMN_NAME_CURRENCY, it.getString("currency"))
                            values.put(COLUMN_NAME_EX_NAME, it.getString("name"))
                            values.put(COLUMN_NAME_ACTIVE, 0)

                            db.insert(TABLE_NAME, null, values)

                        }
                return true

            }
            catch (ex: Exception){
                return  false
            }
        }
    }
}
