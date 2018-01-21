package com.shapps.cryptocompare.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
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
import com.shapps.cryptocompare.Model.ExchangeDetailsDbHelper
import com.shapps.cryptocompare.Model.ExchangeDetailsSchema
import com.shapps.cryptocompare.Model.ExchangeDetailsSchema.ExchangesDetailsEntry.*
import com.shapps.cryptocompare.Model.LiveDataContent
import com.shapps.cryptocompare.Networking.AppController
import com.shapps.cryptocompare.Networking.DetailURLs
import com.shapps.cryptocompare.R
import org.json.JSONArray
import org.json.JSONObject

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * [Charts.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Charts.newInstance] factory method to
 * create an instance of this fragment.
 */
class Charts : Fragment(), View.OnClickListener {

    private var mListener: OnFragmentInteractionListener? = null

    private var lineChart: LineChart? = null

    private lateinit var btn1Hour: Button
    private lateinit var btn1Day: Button
    private lateinit var btn1Week: Button
    private lateinit var btn1Month: Button
    private lateinit var btnAll: Button

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view_main: View = inflater!!.inflate(R.layout.fragment_charts, container, false)


//        insertDataList(view_main)

        val spinner = view_main?.findViewById<Spinner>(R.id.currency_spinner)
        var adapter = ArrayAdapter.createFromResource(activity, R.array.currency_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        if (spinner != null) {
            spinner.adapter = adapter
        }

        btn1Hour = view_main.findViewById(R.id.period_1_hour)
        btn1Hour.setOnClickListener(this)
        btn1Day = view_main.findViewById(R.id.period_1_day)
        btn1Day.setOnClickListener(this)
        btn1Week = view_main.findViewById(R.id.period_1_week)
        btn1Week.setOnClickListener(this)
        btn1Month = view_main.findViewById(R.id.period_1_month)
        btn1Month.setOnClickListener(this)
        btnAll = view_main.findViewById(R.id.period_all)
        btnAll.setOnClickListener(this)

        return view_main
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.period_1_hour -> {
                var x = v as Button
                updateStyle(x)
            }
            R.id.period_1_day -> {
                var x = v as Button
                updateStyle(x)
            }
            R.id.period_1_week -> {
                var x = v as Button
                updateStyle(x)
            }
            R.id.period_1_month -> {
                var x = v as Button
                updateStyle(x)
            }
            R.id.period_all -> {
                var x = v as Button
                updateStyle(x)
            }
            else -> {

            }
        }
    }

    private fun updateStyle(x: Button) {
        removeCurrentStyle()
        x.background = ContextCompat.getDrawable(context, R.drawable.tag_currency_rounded)
        x.setTextColor(Color.WHITE)
    }

    private fun removeCurrentStyle() {
        btn1Hour.background = ContextCompat.getDrawable(context, R.drawable.time_period_rounded)
        btn1Hour.setTextColor(Color.BLACK)
        btn1Day.background = ContextCompat.getDrawable(context, R.drawable.time_period_rounded)
        btn1Day.setTextColor(Color.BLACK)
        btn1Week.background = ContextCompat.getDrawable(context, R.drawable.time_period_rounded)
        btn1Week.setTextColor(Color.BLACK)
        btn1Month.background = ContextCompat.getDrawable(context, R.drawable.time_period_rounded)
        btn1Month.setTextColor(Color.BLACK)
        btnAll.background = ContextCompat.getDrawable(context, R.drawable.time_period_rounded)
        btnAll.setTextColor(Color.BLACK)
    }

//    private fun insertDataList(view_main: View) {
//
//        var id_name_map: HashMap<Int, String> = hashMapOf()
//        var getCurrentExchanges = ""
//
//        val mDbHelper = ExchangeDetailsDbHelper(activity)
//
//        val db = mDbHelper.readableDatabase
//
//        val projection = arrayOf(
//                COLUMN_NAME_ID,
//                COLUMN_NAME_EX_NAME,
//                COLUMN_NAME_CRYPTO_CURRENCY,
//                COLUMN_NAME_CURRENCY)
//
//// Filter results WHERE "title" = 'My Title'
//        val selection = ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_ACTIVE + " = ?"
//        val selectionArgs = arrayOf("1")
//
//// How you want the results sorted in the resulting Cursor
//        val sortOrder = ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_ID + " ASC"
//
//        val cursor = db.query(
//                ExchangeDetailsSchema.ExchangesDetailsEntry.TABLE_NAME, // The table to query
//                projection, // The columns to return
//                selection, // The columns for the WHERE clause
//                selectionArgs, // don't group the rows
//                null, null, // don't filter by row groups
//                sortOrder                                 // The sort order
//        )
//        while (cursor.moveToNext()) {
//            id_name_map.put(cursor.getInt(0), cursor.getString(1))
//            getCurrentExchanges += cursor.getInt(0).toString() + ","
//        }
//        cursor.close()
//
//        if(getCurrentExchanges.length > 1)
//            getCurrentExchanges = getCurrentExchanges.substring(0, getCurrentExchanges.length-1)
//
//        Log.d("Tmp " , getCurrentExchanges)
//
//
//        var pDialog = ProgressDialog(activity)
//        pDialog.setMessage("Loading...")
//        pDialog.show()
//
//        val url = DetailURLs.URL_GET_HISTORY + getCurrentExchanges + "&hours=1"
//
//        val map: HashMap<Int, MutableList<HashMap<String, MutableList<Float>>>> = hashMapOf()
//        val strReq = StringRequest(Request.Method.GET,
//                url, Response.Listener { response ->
//            var historyData = JSONArray(response)
//            LiveDataContent.dumpData()
//            for(i in 0 until historyData.length()){
//                var exchangeCurrent = JSONObject(historyData.get(i).toString())
//                var cryptoCurr = exchangeCurrent.getString("crypto_curr")
//                var currency = exchangeCurrent.getString("curr")
//                var exchangeId = exchangeCurrent.getString("exchange_id")
//                var priceBuy = exchangeCurrent.getString("buy")
//                var priceSell = exchangeCurrent.getString("sell")
//                var date_time = exchangeCurrent.getString("date_time")
//
//                var buyPrice = priceBuy.toFloat()
//                var sellPrice = priceSell.toFloat()
//                if(map.containsKey(exchangeId.toInt())){
//                    map[exchangeId.toInt()]!![0]["buy"]!!.add(buyPrice)
//                    map[exchangeId.toInt()]!![1]["sell"]!!.add(sellPrice)
//                }
//                else{
//                    var buyInit = hashMapOf<String, MutableList<Float>>("buy" to mutableListOf())
//                    map.put(exchangeId.toInt(), mutableListOf())
//                    var sellInit = hashMapOf<String, MutableList<Float>>("sell" to mutableListOf())
//                    map.put(exchangeId.toInt(), mutableListOf())
//                    map[exchangeId.toInt()]!!.add(buyInit)
//                    map[exchangeId.toInt()]!!.add(sellInit)
//                }
//
//            }
//            pDialog.hide()
//            pDialog.dismiss()
//            var entries = ArrayList<Entry>()
//            (0 until map[4]!![0]["buy"]!!.size).mapTo(entries) { Entry(it.toFloat(), map[4]!![0]["buy"]!![it]) }
//            var lds = LineDataSet(entries, "FYB-SG")
//            lds.color = Color.parseColor("#003838")
//            lds.valueTextColor = Color.parseColor("#bbbbbb")
//
//            var entries1 = ArrayList<Entry>()
//            (0 until map[4]!![1]["sell"]!!.size).mapTo(entries1) { Entry(it.toFloat(), map[4]!![1]["sell"]!![it]) }
//            var lds1 = LineDataSet(entries1, "FYB-SG-Sell")
//            lds1.color = Color.parseColor("#01B6AD")
//            lds1.valueTextColor = Color.parseColor("#0A4958")
//
//            var list: List<ILineDataSet> = listOf(lds, lds1)
//
//            // Implement this
//            lineChart = view_main!!.findViewById<View>(R.id.price_chart) as LineChart
//            lineChart!!.data = LineData(list)
//            lineChart!!.invalidate()
//
//
//        }, Response.ErrorListener { error ->
//            VolleyLog.d("TAG ", "Error: " + error.message)
//            pDialog.hide()
//            pDialog.dismiss()
//            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show()
//        })
//
//        // Adding request to request queue
//        AppController.instance?.addToRequestQueue(strReq, "APPLE", activity)
//    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment Charts.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): Charts {
            val fragment = Charts()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}

