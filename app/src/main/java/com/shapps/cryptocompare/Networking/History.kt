package com.shapps.cryptocompare.Networking

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.shapps.cryptocompare.Model.LiveDataContent
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by shyam on 22/11/17.
 */
class History {

    companion object {
        fun draw(siteId: String, siteName: String, term: String, activity: Context, exchange_chart: LineChart, buy: String, sell: String) {
            val url = DetailURLs.URL_GET_HISTORY + siteId + "&" + term


            var pDialog = ProgressDialog(activity)
            pDialog.setMessage("Loading...")
            pDialog.show()

            val map: HashMap<String, MutableList<HashMap<Date, Float>>> = hashMapOf()
            val strReq = StringRequest(Request.Method.GET,
                    url, Response.Listener { response ->
                var historyData = JSONArray(response)
                Log.d("data", historyData.toString())
                LiveDataContent.dumpData()
                for(i in 0 until historyData.length()){
                    var exchangeCurrent = JSONObject(historyData.get(i).toString())
                    var cryptoCurr = exchangeCurrent.getString("crypto_curr")
                    var currency = exchangeCurrent.getString("curr")
                    var exchangeId = exchangeCurrent.getString("exchange_id")
                    var priceBuy = exchangeCurrent.getString("buy")
                    var priceSell = exchangeCurrent.getString("sell")
                    var date_time = exchangeCurrent.getString("date_time")

                    val dateParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'")

                    var formattedDate = dateParser.parse(date_time)

                    Log.d("Date and Price", formattedDate.toString() + " " + priceBuy)

                    var buyPrice = priceBuy.toFloat()
                    var sellPrice = priceSell.toFloat()
                    if(map.containsKey(exchangeId.toInt().toString() + "_buy")){
                        map[exchangeId.toInt().toString() + "_buy"]!!.add(hashMapOf(formattedDate to buyPrice))
                        map[exchangeId.toInt().toString() + "_sell"]!!.add(hashMapOf(formattedDate to sellPrice))
                    }
                    else{
                        var dateBuyVal = hashMapOf(formattedDate to buyPrice)
                        map.put(exchangeId.toInt().toString() + "_buy", arrayListOf(dateBuyVal))
                        var dateSellVal = hashMapOf(formattedDate to sellPrice)
                        map.put(exchangeId.toInt().toString() + "_sell", arrayListOf(dateSellVal))

                    }
                }

                pDialog.hide()
                pDialog.dismiss()

                // If nothing in history add current
                val dateFormatGmt = SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
                dateFormatGmt.timeZone = TimeZone.getTimeZone("UTC")
                val dateFormatLocal = SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
                var currDateTime = dateFormatLocal.parse(dateFormatGmt.format(Date()))
                if(!map.containsKey(siteId.toInt().toString() + "_buy")) {
                    var dateBuyVal = hashMapOf(currDateTime to buy.toFloat())
                    map.put(siteId.toInt().toString() + "_buy", mutableListOf(dateBuyVal))
                }
                if(!map.containsKey(siteId.toInt().toString() + "_buy")) {
                    var dateSellVal = hashMapOf(currDateTime to buy.toFloat())
                    map.put(siteId.toInt().toString() + "_sell", mutableListOf(dateSellVal))
                }

                var entries = ArrayList<Entry>()
                var i = 0f
                for (entry in map[siteId.toInt().toString() + "_buy"]!!) {
                    // FIXME
                    entries.add(Entry(i, entry.values.toFloatArray()[0]))
                    i++
                }
                var lds = LineDataSet(entries, siteName + " Buy")
                lds.color = Color.parseColor("#003838")
                lds.valueTextColor = Color.parseColor("#bbbbbb")

                var entries1 = ArrayList<Entry>()
                i = 0f
                for (entry in map[siteId.toInt().toString() + "_sell"]!!) {
                    // FIXME
                    entries1.add(Entry(i, entry.values.toFloatArray()[0]))
                    i++
                }
                var lds1 = LineDataSet(entries1, siteName + " Sell")
                lds1.color = Color.parseColor("#01B6AD")
                lds1.valueTextColor = Color.parseColor("#0A4958")

                var list: List<ILineDataSet> = listOf(lds, lds1)


                exchange_chart.invalidate()
            }, Response.ErrorListener { error ->
                VolleyLog.d("TAG ", "Error: " + error.message)
                pDialog.hide()
                pDialog.dismiss()
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show()
            })

            // Adding request to request queue
            AppController.instance?.addToRequestQueue(strReq, "APPLE", activity)
        }
    }
}