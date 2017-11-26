package com.shapps.cryptocompare.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.shapps.cryptocompare.Adapters.ExchangesRecyclerView
import com.shapps.cryptocompare.Model.ExchangeDetailsDbHelper
import com.shapps.cryptocompare.Model.ExchangeDetailsSchema

import com.shapps.cryptocompare.Model.LiveDataContent
import com.shapps.cryptocompare.Networking.AppController
import com.shapps.cryptocompare.Networking.DetailURLs
import com.shapps.cryptocompare.R
import org.json.JSONArray
import org.json.JSONObject

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class Dashboard : Fragment() {
    // TODO: Customize parameters
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    private var viewAdap: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_dashboard, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            if (mColumnCount <= 1) {
                view.layoutManager = LinearLayoutManager(context)
            } else {
                view.layoutManager = GridLayoutManager(context, mColumnCount)
            }
            view.adapter = ExchangesRecyclerView(LiveDataContent.ITEMS, mListener)
            viewAdap = view.adapter
            insertDataIntoAdapter()
        }
        return view
    }

    private fun insertDataIntoAdapter() {

        var id_name_map: HashMap<Int, String> = hashMapOf()

        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        var getCurrentExchanges = ""

        val mDbHelper = ExchangeDetailsDbHelper(activity)

        val db = mDbHelper.readableDatabase

        val projection = arrayOf(
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_ID,
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_EX_NAME)

// Filter results WHERE "title" = 'My Title'
        val selection = ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_ACTIVE + " = ?"
        val selectionArgs = arrayOf("1")

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
        while (cursor.moveToNext()) {
            id_name_map.put(cursor.getInt(0), cursor.getString(1))
            getCurrentExchanges += cursor.getInt(0).toString() + ","
        }
        cursor.close()

        if(getCurrentExchanges.length > 1)
            getCurrentExchanges = getCurrentExchanges.substring(0, getCurrentExchanges.length-1)

        Log.d("Tmp " , getCurrentExchanges)


        var pDialog = ProgressDialog(activity)
        pDialog.setMessage("Loading...")
        pDialog.show()

        val url = DetailURLs.URL_GET_CURRENT + getCurrentExchanges
        val strReq = StringRequest(Request.Method.GET,
                url, Response.Listener { response ->
            var currentData = JSONArray(response)
            LiveDataContent.dumpData()

            for(i in 0 until currentData.length()){
                var exchangeCurrent = JSONObject(currentData.get(i).toString())
                var cryptoCurr = exchangeCurrent.getString("crypto_curr")
                var currency = exchangeCurrent.getString("curr")
                var exchangeId = exchangeCurrent.getString("exchange_id")
                var priceBuy = exchangeCurrent.getString("buy")
                var priceSell = exchangeCurrent.getString("sell")
                var volume = exchangeCurrent.getString("volume")
                var timeInt = prefs.getString("pref_key_storage_min_max_period", "1").toInt()
                var lowBuy = ""
                var highBuy = ""
                var lowSell = ""
                var highSell = ""
                when (timeInt) {
                    1 -> {
                        lowBuy = exchangeCurrent.getString("last_hour_min_buy")
                        highBuy = exchangeCurrent.getString("last_hour_max_buy")
                        lowSell = exchangeCurrent.getString("last_hour_min_sell")
                        highSell = exchangeCurrent.getString("last_hour_max_sell")
                    }
                    2 -> {
                        lowBuy = exchangeCurrent.getString("last_day_min_buy")
                        highBuy = exchangeCurrent.getString("last_day_max_buy")
                        lowSell = exchangeCurrent.getString("last_day_min_sell")
                        highSell = exchangeCurrent.getString("last_day_max_sell")
                    }
                    3 -> {
                        lowBuy = exchangeCurrent.getString("last_week_min_buy")
                        highBuy = exchangeCurrent.getString("last_week_max_buy")
                        lowSell = exchangeCurrent.getString("last_week_min_sell")
                        highSell = exchangeCurrent.getString("last_week_max_sell")
                    }
                    4 -> {
                        lowBuy = exchangeCurrent.getString("last_month_min_buy")
                        highBuy = exchangeCurrent.getString("last_month_max_buy")
                        lowSell = exchangeCurrent.getString("last_month_min_sell")
                        highSell = exchangeCurrent.getString("last_month_max_sell")
                    }
                }

                var ex_name = id_name_map[exchangeId.toInt()]

                LiveDataContent.addItem(LiveDataContent.LiveData(i.toString(), cryptoCurr, currency , exchangeId,
                        ex_name!!, priceBuy, priceSell, volume, lowBuy, highBuy, lowSell, highSell))
            }
            pDialog.hide()
            pDialog.dismiss()
            viewAdap!!.notifyDataSetChanged()
        }, Response.ErrorListener { error ->
            VolleyLog.d("TAG ", "Error: " + error.message)
            pDialog.hide()
            pDialog.dismiss()
            Toast.makeText(activity, "Network Error", Toast.LENGTH_SHORT).show()
        })

        // Adding request to request queue
        AppController.instance?.addToRequestQueue(strReq, "APPLE", activity)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
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
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(cryptoCurrency: String, currency: String, exchangeId: String, exchangeName: String, buy: String, sell: String,
                                      buyLow: String, buyHigh: String, sellLow: String, sellHigh: String, vol: String)
    }

    companion object {

        // TODO: Customize parameter argument names
        private val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance(): Dashboard {
            //            val args = Bundle()
//            args.putInt(ARG_COLUMN_COUNT, columnCount)
//            fragment.arguments = args
            return Dashboard()
        }
    }
}
