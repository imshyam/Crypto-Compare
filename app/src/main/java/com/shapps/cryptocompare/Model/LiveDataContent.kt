package com.shapps.cryptocompare.Model

import android.app.ProgressDialog
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

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createLiveData(i))
        }
    }

    private fun addItem(item: LiveData) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createLiveData(position: Int): LiveData {

        return LiveData(position.toString(), "Bitcoin", "SGD " + position, "1",
                "Fyb-SG", "2321.22", "2100.33")
    }

    class LiveData(val id: String, val cryptoCurrency: String, val currency: String, val exchangeId: String, val exchangeName: String,
                   val priceBuy: String, val priceSell: String)

    fun getData(activityContext: Main) {
        Log.d("Load", "Ing")

        var url = DetailURLs.URL_GET_CURRENT

        val strReq = StringRequest(Request.Method.GET,
                url, Response.Listener { response ->
            var currentData = JSONArray(response)

            Log.d("currentData : ", currentData.toString())
            Log.d("Load", "Done")
        }, Response.ErrorListener { error ->
            VolleyLog.d("TAG ", "Error: " + error.message)
        })

        // Adding request to request queue
        AppController.instance?.addToRequestQueue(strReq, "APPLE", activityContext)

    }
}
