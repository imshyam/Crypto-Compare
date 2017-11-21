package com.shapps.cryptocompare.Networking

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
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
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Created by shyam on 22/11/17.
 */
class History {

    companion object {
        fun draw(siteId: String, siteName: String, term: String, activity: Context, exchange_chart: LineChart) :HashMap<Int, MutableList<HashMap<String, MutableList<Float>>>>{
            val url = DetailURLs.URL_GET_HISTORY + siteId + "&" + term


            var pDialog = ProgressDialog(activity)
            pDialog.setMessage("Loading...")
            pDialog.show()

            val map: HashMap<Int, MutableList<HashMap<String, MutableList<Float>>>> = hashMapOf()
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

                    var buyPrice = priceBuy.toFloat()
                    var sellPrice = priceSell.toFloat()
                    if(map.containsKey(exchangeId.toInt())){
                        map[exchangeId.toInt()]!![0]["buy"]!!.add(buyPrice)
                        map[exchangeId.toInt()]!![1]["sell"]!!.add(sellPrice)
                    }
                    else{
                        var buyInit = hashMapOf<String, MutableList<Float>>("buy" to mutableListOf())
                        map.put(exchangeId.toInt(), mutableListOf())
                        var sellInit = hashMapOf<String, MutableList<Float>>("sell" to mutableListOf())
                        map.put(exchangeId.toInt(), mutableListOf())
                        map[exchangeId.toInt()]!!.add(buyInit)
                        map[exchangeId.toInt()]!!.add(sellInit)
                    }
                }

                pDialog.hide()
                pDialog.dismiss()

                var entries = ArrayList<Entry>()
                (0 until map[siteId.toInt()]!![0]["buy"]!!.size).mapTo(entries) { Entry(it.toFloat(), map[siteId.toInt()]!![0]["buy"]!![it]) }
                var lds = LineDataSet(entries, siteName + " Buy")
                lds.color = Color.parseColor("#003838")
                lds.valueTextColor = Color.parseColor("#bbbbbb")

                var entries1 = ArrayList<Entry>()
                (0 until map[siteId.toInt()]!![1]["sell"]!!.size).mapTo(entries1) { Entry(it.toFloat(), map[siteId.toInt()]!![1]["sell"]!![it]) }
                var lds1 = LineDataSet(entries1, siteName + " Sell")
                lds1.color = Color.parseColor("#01B6AD")
                lds1.valueTextColor = Color.parseColor("#0A4958")

                var list: List<ILineDataSet> = listOf(lds, lds1)

                exchange_chart.data = LineData(list)
                exchange_chart.invalidate()
            }, Response.ErrorListener { error ->
                VolleyLog.d("TAG ", "Error: " + error.message)
                pDialog.hide()
                pDialog.dismiss()
                Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show()
            })

            // Adding request to request queue
            AppController.instance?.addToRequestQueue(strReq, "APPLE", activity)

            return map
        }
    }
}