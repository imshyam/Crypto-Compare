package com.shapps.cryptocompare.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class Charts : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    /**
     * Shared Preferences File Name
     */
    private val PREF_FILE = "ExchangesList"

    private val NO_OF_BITCOIN_EXCHANGES = 19
    private val NO_OF_ETHEREUM_EXCHANGES = 4

    private var lineChart: LineChart? = null
    private var view_main: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        insertDataList()

        view_main = inflater!!.inflate(R.layout.fragment_charts, container, false)

        return view_main
    }

    private fun insertDataList() {

        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        var getCurrentExchanges = ""
        for (i in 1..NO_OF_BITCOIN_EXCHANGES) {
            var x = prefs.getBoolean("pref_key_storage_bitcoin_exchanges_" + i.toString(), false)
            if(x) {
                getCurrentExchanges += i.toString()
                if(i < NO_OF_BITCOIN_EXCHANGES) getCurrentExchanges += ","
            }
        }
//        for (i in 1001..1000+NO_OF_ETHEREUM_EXCHANGES) {
//            var x = prefs.getBoolean("pref_key_storage_ethereum_exchanges_" + i.toString(), false)
//            if(x) {
//                getCurrentExchanges += i.toString()
//                if(i < 1000+NO_OF_ETHEREUM_EXCHANGES) getCurrentExchanges += ","
//            }
//        }

        var pDialog = ProgressDialog(activity)
        pDialog.setMessage("Loading...")
        pDialog.show()

        val url = DetailURLs.URL_GET_HISTORY + getCurrentExchanges + "?hours=1"

        val map: HashMap<Int, HashMap<String, FloatArray>> = hashMapOf(1 to hashMapOf("buy" to FloatArray(10), "sell" to FloatArray(10)),
                2 to hashMapOf("buy" to FloatArray(50), "sell" to FloatArray(50)))
        val strReq = StringRequest(Request.Method.GET,
                url, Response.Listener { response ->
            var currentData = JSONArray(response)
            LiveDataContent.dumpData()
            var a = 0
            var b = 0
            for(i in 0 until currentData.length()){
                var exchangeCurrent = JSONObject(currentData.get(i).toString())
                var cryptoCurr = exchangeCurrent.getString("crypto_curr")
                var currency = exchangeCurrent.getString("curr")
                var exchangeId = exchangeCurrent.getString("exchange_id")
                var priceBuy = exchangeCurrent.getString("buy")
                var priceSell = exchangeCurrent.getString("sell")

//                var sharedPref = activity.getSharedPreferences(PREF_FILE, 0)
//                var name = sharedPref.getString(exchangeId, "No Name Found")
                if(exchangeId == "1") {
                    map[1]!!["buy"]!!.set(a, priceBuy.toFloat())
                    map[1]!!["sell"]!!.set(a, priceSell.toFloat())
                    a++
                } else if(exchangeId == "2") {
//                    map[2]!!["buy"]!!.set(b, priceBuy.toFloat())
//                    map[2]!!["sell"]!!.set(b, priceSell.toFloat())
                    b++
                }

            }
            pDialog.hide()
            var entries = ArrayList<Entry>()
            (0 until 10).mapTo(entries) { Entry(it.toFloat(), map[1]!!["buy"]!![it]) }
            var lds = LineDataSet(entries, "FYB-SG")
            lds.color = Color.parseColor("#003838")
            lds.valueTextColor = Color.parseColor("#bbbbbb")

            var entries1 = ArrayList<Entry>()
            (0 until 10).mapTo(entries) { Entry(it.toFloat(), map[1]!!["sell"]!![it]) }
            var lds1 = LineDataSet(entries1, "FYB-SG-Sell")
            lds1.color = Color.parseColor("#01B6AD")
            lds1.valueTextColor = Color.parseColor("#0A4958")

            var list: List<ILineDataSet> = listOf(lds, lds1)

            // Implement this
            lineChart = view_main!!.findViewById<View>(R.id.price_chart) as LineChart
            lineChart!!.data = LineData(list)
            lineChart!!.invalidate()


        }, Response.ErrorListener { error ->
            VolleyLog.d("TAG ", "Error: " + error.message)
        })

        // Adding request to request queue
        AppController.instance?.addToRequestQueue(strReq, "APPLE", activity)
    }

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

