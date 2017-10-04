package com.shapps.cryptocompare.Model

import android.preference.SwitchPreference
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.shapps.cryptocompare.Activities.Main
import com.shapps.cryptocompare.Networking.AppController
import com.shapps.cryptocompare.Networking.DetailURLs
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by shyam on 23/7/17.
 */

object LiveDataContent {

    /**
     * An array of sample items.
     */
    val ITEMS: MutableList<LiveData> = ArrayList()

    /**
     * A map of sample items, by ID.
     */
    private val ITEM_MAP: MutableMap<String, LiveData> = HashMap()

    init {
        // Add some sample items.
    }

    private fun addItem(item: LiveData) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    class LiveData(val id: String, val cryptoCurrency: String, val currency: String, val exchangeId: String, val exchangeName: String,
                   val priceBuy: String, val priceSell: String)

    fun getData(activityContext: Main, currentExchanges: String) {
        Log.d("Load", "Ing")

        val url = DetailURLs.URL_GET_CURRENT + currentExchanges

        val strReq = StringRequest(Request.Method.GET,
                url, Response.Listener { response ->
            var currentData = JSONArray(response)
            for(i in 0..currentData.length()-1){
                var exchangeCurrent = JSONObject(currentData.get(i).toString())
                var cryptoCurr = exchangeCurrent.getString("crypto_curr")
                var currency = exchangeCurrent.getString("curr")
                var exchangeId = exchangeCurrent.getString("exchange_id")
                var priceBuy = exchangeCurrent.getString("buy")
                var priceSell = exchangeCurrent.getString("sell")
                addItem(LiveData(i.toString(), cryptoCurr, currency , exchangeId,
                        "Fyb-SG", priceBuy, priceSell))
            }
            Log.d("currentData : ", currentData.toString())
            Log.d("Load", "Done")
        }, Response.ErrorListener { error ->
            VolleyLog.d("TAG ", "Error: " + error.message)
        })

        // Adding request to request queue
        AppController.instance?.addToRequestQueue(strReq, "APPLE", activityContext)

    }
}
