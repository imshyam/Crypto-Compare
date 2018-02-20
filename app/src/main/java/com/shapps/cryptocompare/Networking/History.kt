package com.shapps.cryptocompare.Networking

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.shapps.cryptocompare.Model.XAxisValuesArray
import com.shapps.cryptocompare.R
import kotlinx.android.synthetic.main.activity_details.view.*
import java.util.concurrent.TimeUnit


/**
 * Created by shyam on 22/11/17.
 */
class History {

    companion object {
        fun draw(siteId: String, siteName: String, term: String, activity: Context, view: View, siteId2: String, siteName2: String, isDifferentCurrency: Boolean, fee1: Float, fee2: Float) {

            var currentTimeTag = ""
            var currentPriceTag = ""

            val exchangeChart = view.findViewById<LineChart>(R.id.exchange_chart)


            var priceSelected = view.findViewById<TextView>(R.id.price_selected)
            var timeSelected = view.findViewById<TextView>(R.id.time_selected)

            exchangeChart.setDrawMarkers(false)

            exchangeChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onNothingSelected() {
                    timeSelected.text = currentTimeTag
                    priceSelected.text = currentPriceTag
                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    timeSelected.text = exchangeChart.xAxis.valueFormatter.getFormattedValue(e!!.x, exchangeChart.xAxis)
                    priceSelected.text = h!!.y.toString()
                }
            })

            val priceText = view.findViewById<TextView>(R.id.price_selected)
            val currencyText = view.findViewById<TextView>(R.id.currency_selected)
            val timeText = view.findViewById<TextView>(R.id.time_selected)

            var url = DetailURLs.URL_GET_HISTORY + siteId + "&" + term

            if(siteId2.isNotEmpty() && siteId != siteId2){
                url = DetailURLs.URL_GET_HISTORY + siteId + "," + siteId2 + "&" + term
            }

            var pDialog = ProgressDialog(activity)
            pDialog.setMessage("Loading...")
            pDialog.show()

            var currentData: JSONArray

            val map: HashMap<String, MutableList<HashMap<Date, Float>>> = hashMapOf()
            val strReq = StringRequest(Request.Method.GET,
                    url, Response.Listener { response ->
                var allData = JSONObject(response)
                currentData = allData.getJSONArray("current")
                if (currentData.length() == 1 || currentData[0] == currentData[1]){
                    val currentDt = currentData.getJSONObject(0)
                    val currentBuyWithFee = currentDt.getString("buy").toFloat() * (1 + fee1/100)
                    val curr = currentDt.getString("curr")
                    val currentTime = currentDt.getString("date_time")
                    priceText.text = currentBuyWithFee.toString()
                    currencyText.text = curr
                    timeText.text = currentTime

                    currentPriceTag = currentBuyWithFee.toString()
                    currentTimeTag = currentTime
                }
                else {
                    val currentDt = currentData.getJSONObject(0)
                    var currentBuy1WithFee = 0f
                    var currentTime = ""
                    var currentSell2WithFee = 0f
                    if(currentDt.getInt("exchange_id") == siteId.toInt()){
                        currentBuy1WithFee = currentDt.getString("buy").toFloat() * (1 + fee1/100)
                        currentTime = currentDt.getString("date_time")

                        val currentDt2 = currentData.getJSONObject(1)
                        currentSell2WithFee = currentDt2.getString("sell").toFloat() * (1 + fee2/100)
                    }
                    else {
                        currentSell2WithFee = currentDt.getString("sell").toFloat() * (1 + fee1/100)
                        currentTime = currentDt.getString("date_time")

                        val currentDt2 = currentData.getJSONObject(1)
                        currentBuy1WithFee= currentDt2.getString("buy").toFloat() * (1 + fee2/100)
                    }

                    timeText.text = currentTime
                    currentTimeTag = currentTime

                    if(!isDifferentCurrency){
                        val diffB = currentSell2WithFee - currentBuy1WithFee
                        priceText.text = diffB.toString()
                        currencyText.text = "Difference"
                        currentPriceTag = diffB.toString()
                    }
                    else{
                        val rate = currentSell2WithFee / currentBuy1WithFee
                        priceText.text = rate.toString()
                        currencyText.text = "Rate"
                        currentPriceTag = rate.toString()
                    }

                }

                var historyData = allData.getJSONArray("history")
                LiveDataContent.dumpData()
                var buy = 100f
                var sell = 200f
                for(i in 0 until historyData.length()){
                    var exchangeCurrent = JSONObject(historyData.get(i).toString())
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
                    var dateBuyVal = hashMapOf(currDateTime to buy)
                    map.put(siteId.toInt().toString() + "_buy", mutableListOf(dateBuyVal))
                }
                if(!map.containsKey(siteId.toInt().toString() + "_sell")) {
                    var dateSellVal = hashMapOf(currDateTime to sell)
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
                        val sellWithFee = entrySell.values.toFloatArray()[0] * (1 - fee2/100)
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

                exchangeChart.data = LineData(list)

                var xAxis = exchangeChart.xAxis

                xAxis.valueFormatter = object : IAxisValueFormatter {

                    private val mFormat = SimpleDateFormat("dd MMM HH:mm")

                    override fun getFormattedValue(value: Float, axis: AxisBase): String {

                        val x = value.toLong()

                        return mFormat.format(Date(x))
                    }
                }
                exchangeChart.invalidate()
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