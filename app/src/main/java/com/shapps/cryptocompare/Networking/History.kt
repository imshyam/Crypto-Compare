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
import com.github.mikephil.charting.components.AxisBase
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
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.shapps.cryptocompare.Model.XAxisValuesArray
import java.util.concurrent.TimeUnit


/**
 * Created by shyam on 22/11/17.
 */
class History {

    companion object {
        fun draw(siteId: String, siteName: String, term: String, activity: Context, exchange_chart: LineChart, buy: String, sell: String, siteId2: String, siteName2: String, isDifferentCurrency: Boolean, fee1: Float, fee2: Float) {
            var url = DetailURLs.URL_GET_HISTORY + siteId + "&" + term

            if(siteId2.isNotEmpty() && siteId != siteId2){
                url = DetailURLs.URL_GET_HISTORY + siteId + "," + siteId2 + "&" + term
            }

            var pDialog = ProgressDialog(activity)
            pDialog.setMessage("Loading...")
            pDialog.show()

            val map: HashMap<String, MutableList<HashMap<Date, Float>>> = hashMapOf()
            val strReq = StringRequest(Request.Method.GET,
                    url, Response.Listener { response ->
                var historyData = JSONArray(response)
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

                // If nothing in history, add current
                val dateFormatGmt = SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
                dateFormatGmt.timeZone = TimeZone.getTimeZone("UTC")
                val dateFormatLocal = SimpleDateFormat("yyyy-MMM-dd HH:mm:ss")
                var currDateTime = dateFormatLocal.parse(dateFormatGmt.format(Date()))

                if(!map.containsKey(siteId.toInt().toString() + "_buy")) {
                    var dateBuyVal = hashMapOf(currDateTime to buy.toFloat())
                    map.put(siteId.toInt().toString() + "_buy", mutableListOf(dateBuyVal))
                }
                if(!map.containsKey(siteId.toInt().toString() + "_sell")) {
                    var dateSellVal = hashMapOf(currDateTime to sell.toFloat())
                    map.put(siteId.toInt().toString() + "_sell", mutableListOf(dateSellVal))
                }

                // Add to entries
                var lds: LineDataSet
                var lds1: LineDataSet
                var list: List<ILineDataSet>
                if(!siteId2.isNotEmpty() || siteId == siteId2) {
                    var entries = ArrayList<Entry>()
                    var i = 0f
                    for (entry in map[siteId.toInt().toString() + "_buy"]!!) {
                        var timestamp = Timestamp(entry.keys.elementAt(0).time).time
                        entries.add(Entry(timestamp.toFloat(), entry.values.toFloatArray()[0]))
                        i++
                    }
                    lds = LineDataSet(entries, siteName + " Buy")
                    lds.color = Color.parseColor("#003838")
                    lds.valueTextColor = Color.parseColor("#bbbbbb")

                    var entries1 = ArrayList<Entry>()
                    i = 0f
                    for (entry in map[siteId.toInt().toString() + "_sell"]!!) {
                        var timestamp = Timestamp(entry.keys.elementAt(0).time).time
                        entries1.add(Entry(timestamp.toFloat(), entry.values.toFloatArray()[0]))
                        i++
                    }
                    lds1 = LineDataSet(entries1, siteName + " Sell")
                    lds1.color = Color.parseColor("#01B6AD")
                    lds1.valueTextColor = Color.parseColor("#0A4958")

                    list = listOf(lds, lds1)
                }

                // If comparing Two
                else {
                    val entries = ArrayList<Entry>()
                    val totalItems = if (map[siteId.toInt().toString() + "_buy"]!!.size < map[siteId2.toInt().toString() + "_buy"]!!.size)
                        map[siteId.toInt().toString() + "_buy"]!!.size
                    else
                        map[siteId2.toInt().toString() + "_buy"]!!.size
                    for (i in 0 until totalItems){
                        val entryBuy = map[siteId.toInt().toString() + "_buy"]!![i]
                        val entrySell = map[siteId2.toInt().toString() + "_sell"]!![i]
                        val buyWithFee = entryBuy.values.toFloatArray()[0] * (1 + fee1/100)
                        val sellWithFee = entrySell.values.toFloatArray()[0] * (1 - fee1/100)
                        val valueInsert: Float = if(isDifferentCurrency)
                            sellWithFee / buyWithFee
                        else {
                            sellWithFee - buyWithFee
                        }
                        val timestamp = Timestamp(entryBuy.keys.elementAt(0).time).time
                        entries.add(Entry(timestamp.toFloat(), valueInsert))
                    }

                    lds = if(isDifferentCurrency)
                        LineDataSet(entries, "Currency rate Graph for Buy at $siteName and sell at $siteName2")
                    else
                        LineDataSet(entries, "Margin Graph for Buy at $siteName and sell at $siteName2")
                    lds.color = Color.parseColor("#01B6AD")
                    lds.valueTextColor = Color.parseColor("#0A4958")

                    list = listOf(lds)
                }

                exchange_chart.data = LineData(list)

                var xAxis = exchange_chart.xAxis

                xAxis.valueFormatter = object : IAxisValueFormatter {

                    private val mFormat = SimpleDateFormat("dd MMM HH:mm")

                    override fun getFormattedValue(value: Float, axis: AxisBase): String {

                        val x = value.toLong()

                        return mFormat.format(Date(x))
                    }
                }

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