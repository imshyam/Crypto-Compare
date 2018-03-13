package com.shapps.cryptocompare.fragments

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RemoteViews
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.shapps.cryptocompare.activities.Settings
import com.shapps.cryptocompare.adapters.ExchangesRecyclerView
import com.shapps.cryptocompare.model.ExchangeDetailsDbHelper
import com.shapps.cryptocompare.model.ExchangeDetailsSchema

import com.shapps.cryptocompare.model.LiveDataContent
import com.shapps.cryptocompare.networking.AppController
import com.shapps.cryptocompare.networking.DetailURLs
import com.shapps.cryptocompare.R
import com.shapps.cryptocompare.UpdateService
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

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

    private lateinit var notificationManager: NotificationManager

    private lateinit var getCurrentExchanges: String
    private lateinit var id_name_map: HashMap<Int, String>

    private lateinit var viewNotification: RemoteViews
    private lateinit var notifyBuilder: NotificationCompat.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }

        // Add Notification
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //notification view
        viewNotification = RemoteViews(
                context.packageName,
                R.layout.price_notification_item
        )

        //notification intent
        val notificationIntent = Intent(context, UpdateService::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
        viewNotification.setOnClickPendingIntent(R.id.refresh_notification,
                PendingIntent.getService(context, 0, notificationIntent.setAction(ACTION_REFRESH), 0))
        viewNotification.setOnClickPendingIntent(R.id.notification_content,
                PendingIntent.getService(context, 0, notificationIntent.setAction(ACTION_OPEN_APP), 0))

        // TODO
        notifyBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_refresh_black_24dp)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Price List")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View
        if(insertAndCheckData()) {
            view = inflater!!.inflate(R.layout.fragment_dashboard, container, false)
        } else {
            view = inflater!!.inflate(R.layout.no_exchange_selected, container, false)
            val addExchange = view.findViewById<Button>(R.id.add_exchange)
            addExchange.setOnClickListener{
                val settingsAct = Intent(context, Settings::class.java)
                startActivity(settingsAct)
            }
            return view
        }

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

    private fun insertAndCheckData(): Boolean {
        id_name_map = hashMapOf()

        getCurrentExchanges = ""

        val mDbHelper = ExchangeDetailsDbHelper(activity)

        val db = mDbHelper.readableDatabase

        val projection = arrayOf(
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_ID,
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_CRYPTO_CURRENCY,
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_CURRENCY,
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
            id_name_map.put(cursor.getInt(0), cursor.getString(3))
            getCurrentExchanges += cursor.getInt(0).toString() + ","
        }
        cursor.close()

        if(getCurrentExchanges.length > 1) {
            getCurrentExchanges = getCurrentExchanges.substring(0, getCurrentExchanges.length - 1)
            return true
        }

        return false
    }

    private fun insertDataIntoAdapter() {

        var pDialog = ProgressDialog(activity)
        pDialog.setMessage("Loading...")
        pDialog.show()

        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)

        val url = DetailURLs.URL_GET_CURRENT + getCurrentExchanges
        val strReq = StringRequest(Request.Method.GET,
                url, Response.Listener { response ->
            var currentData = JSONArray(response)
            LiveDataContent.dumpData()

            var exchangeWithNewLine = ""
            var buyWithNewLine = ""
            var sellWithNewLine = ""

            for(i in 0 until currentData.length()){
                var exchangeCurrent = JSONObject(currentData.get(i).toString())
                var cryptoCurr = exchangeCurrent.getString("crypto_curr")
                var currency = exchangeCurrent.getString("curr")
                var exchangeId = exchangeCurrent.getString("exchange_id")
                var priceBuy = exchangeCurrent.getString("buy").roundTo2DecimalPlaces()
                var priceSell = exchangeCurrent.getString("sell").roundTo2DecimalPlaces()
                var volume = exchangeCurrent.getString("volume").roundTo2DecimalPlaces()
                var timeInt = prefs.getString("pref_key_storage_min_max_period", "1").toInt()
                var lowBuy = ""
                var highBuy = ""
                var lowSell = ""
                var highSell = ""
                when (timeInt) {
                    1 -> {
                        lowBuy = exchangeCurrent.getString("last_hour_min_buy").roundTo2DecimalPlaces()
                        highBuy = exchangeCurrent.getString("last_hour_max_buy").roundTo2DecimalPlaces()
                        lowSell = exchangeCurrent.getString("last_hour_min_sell").roundTo2DecimalPlaces()
                        highSell = exchangeCurrent.getString("last_hour_max_sell").roundTo2DecimalPlaces()
                    }
                    2 -> {
                        lowBuy = exchangeCurrent.getString("last_day_min_buy").roundTo2DecimalPlaces()
                        highBuy = exchangeCurrent.getString("last_day_max_buy").roundTo2DecimalPlaces()
                        lowSell = exchangeCurrent.getString("last_day_min_sell").roundTo2DecimalPlaces()
                        highSell = exchangeCurrent.getString("last_day_max_sell").roundTo2DecimalPlaces()
                    }
                    3 -> {
                        lowBuy = exchangeCurrent.getString("last_week_min_buy").roundTo2DecimalPlaces()
                        highBuy = exchangeCurrent.getString("last_week_max_buy").roundTo2DecimalPlaces()
                        lowSell = exchangeCurrent.getString("last_week_min_sell").roundTo2DecimalPlaces()
                        highSell = exchangeCurrent.getString("last_week_max_sell").roundTo2DecimalPlaces()
                    }
                    4 -> {
                        lowBuy = exchangeCurrent.getString("last_month_min_buy").roundTo2DecimalPlaces()
                        highBuy = exchangeCurrent.getString("last_month_max_buy").roundTo2DecimalPlaces()
                        lowSell = exchangeCurrent.getString("last_month_min_sell").roundTo2DecimalPlaces()
                        highSell = exchangeCurrent.getString("last_month_max_sell").roundTo2DecimalPlaces()
                    }
                }

                val ex_name = id_name_map[exchangeId.toInt()]

                exchangeWithNewLine += cryptoCurr + " - " +
                        ex_name +  "\n"
                buyWithNewLine += currency + " " + priceBuy + "\n"
                sellWithNewLine += priceSell + "\n"

                LiveDataContent.addItem(LiveDataContent.LiveData(i.toString(), cryptoCurr, currency , exchangeId,
                        ex_name!!, priceBuy, priceSell, volume, lowBuy, highBuy, lowSell, highSell))
            }
            pDialog.hide()
            pDialog.dismiss()
            viewAdap!!.notifyDataSetChanged()


            if(exchangeWithNewLine.isNotEmpty()) {
                exchangeWithNewLine = exchangeWithNewLine.substring(0, exchangeWithNewLine.length - 1)
                buyWithNewLine = buyWithNewLine.substring(0, buyWithNewLine.length - 1)
                sellWithNewLine = sellWithNewLine.substring(0, sellWithNewLine.length - 1)
            }

            val sdf = SimpleDateFormat("HH:mm a")
            viewNotification.setTextViewText(R.id.time_ni, sdf.format(Calendar.getInstance().time))
            viewNotification.setTextViewText(R.id.exchange_name_ni, exchangeWithNewLine)
            viewNotification.setTextViewText(R.id.buy_price_ni, buyWithNewLine)
            viewNotification.setTextViewText(R.id.sell_price_ni, sellWithNewLine)

            notifyBuilder.setCustomBigContentView(viewNotification)

            notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build())

        }, Response.ErrorListener { error ->
            VolleyLog.d("TAG ", "Error: " + error.message)
            pDialog.hide()
            pDialog.dismiss()
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

        const val ACTION_REFRESH = "REFRESH"
        const val ACTION_OPEN_APP = "OPEN_APP"
        const val NOTIFICATION_ID = 0
    }

    private fun String.roundTo2DecimalPlaces() =
            BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble().toString()
}
