package com.shapps.cryptocompare

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest
import com.shapps.cryptocompare.activities.Main
import com.shapps.cryptocompare.fragments.Dashboard
import com.shapps.cryptocompare.fragments.Dashboard.Companion.NOTIFICATION_ID
import com.shapps.cryptocompare.model.ExchangeDetailsDbHelper
import com.shapps.cryptocompare.model.ExchangeDetailsSchema
import com.shapps.cryptocompare.model.LiveDataContent
import com.shapps.cryptocompare.networking.AppController
import com.shapps.cryptocompare.networking.DetailURLs
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by shyam on 2/3/18.
 */
class UpdateService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private lateinit var context: Context
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action.equals(Dashboard.ACTION_REFRESH)){

            updateNotification("Updated: updating", "Loading...", "", "")

            var getCurrentExchanges = ""
            val id_name_map: HashMap<Int, String> = hashMapOf()

            val mDbHelper = ExchangeDetailsDbHelper(context)

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
            }

            val prefs = PreferenceManager.getDefaultSharedPreferences(context)

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
                    var timeInt = prefs.getString("pref_key_storage_min_max_period", "1").toInt()

                    val ex_name = id_name_map[exchangeId.toInt()]

                    exchangeWithNewLine += cryptoCurr + " - " +
                            ex_name +  "\n"
                    buyWithNewLine += currency + " " + priceBuy + "\n"
                    sellWithNewLine += priceSell + "\n"

                }


                if(exchangeWithNewLine.isNotEmpty()) {
                    exchangeWithNewLine = exchangeWithNewLine.substring(0, exchangeWithNewLine.length - 1)
                    buyWithNewLine = buyWithNewLine.substring(0, buyWithNewLine.length - 1)
                    sellWithNewLine = sellWithNewLine.substring(0, sellWithNewLine.length - 1)
                }
                updateNotification("Yo", exchangeWithNewLine, buyWithNewLine, sellWithNewLine)

            }, Response.ErrorListener { error ->
                VolleyLog.d("TAG ", "Error: " + error.message)
                updateNotification("Updated: Error", "Error Updating. Try again.", "", "")
            })

            // Adding request to request queue
            AppController.instance?.addToRequestQueue(strReq, "APPLE", context)

        }
        else if(intent?.action.equals(Dashboard.ACTION_OPEN_APP)) {
            // Cancel Notification
            var mainIntent = Intent(context, Main::class.java)
            startActivity(mainIntent)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateNotification(time: String, exchangeList: String, buyPriceList: String, sellPriceList: String) {
        //notification view
        val viewNotification = RemoteViews(
                context.packageName,
                R.layout.price_notification_item
        )

        //notification intent
        val notificationIntent = Intent(context, UpdateService::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
        viewNotification.setOnClickPendingIntent(R.id.refresh_notification,
                PendingIntent.getService(context, 0, notificationIntent.setAction(Dashboard.ACTION_REFRESH), 0))
        viewNotification.setOnClickPendingIntent(R.id.notification_content,
                PendingIntent.getService(context, 0, notificationIntent.setAction(Dashboard.ACTION_OPEN_APP), 0))

        // TODO
        val notifyBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_refresh_black_24dp)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Price List")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
        viewNotification.setTextViewText(R.id.time_ni, time)
        viewNotification.setTextViewText(R.id.exchange_name_ni, exchangeList)
        viewNotification.setTextViewText(R.id.buy_price_ni, buyPriceList)
        viewNotification.setTextViewText(R.id.sell_price_ni, sellPriceList)

        notifyBuilder.setCustomBigContentView(viewNotification)

        // Add Notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    private fun String.roundTo2DecimalPlaces() =
            BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble().toString()
}