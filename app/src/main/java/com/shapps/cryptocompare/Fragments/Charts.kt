package com.shapps.cryptocompare.Fragments

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.github.mikephil.charting.charts.LineChart
import com.shapps.cryptocompare.Model.ExchangeDetailsDbHelper
import com.shapps.cryptocompare.Model.ExchangeDetailsSchema
import com.shapps.cryptocompare.Networking.History
import com.shapps.cryptocompare.R

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * [Charts.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Charts.newInstance] factory method to
 * create an instance of this fragment.
 */

// FIXME Fix passing current buy sell price
class Charts : Fragment(), View.OnClickListener, OnItemSelectedListener {

    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var lineChart: LineChart

    private lateinit var currencySpinner: Spinner
    private lateinit var exchangeSpinner: Spinner
    private lateinit var listIdsForCurrentSettings: MutableList<String>

    private lateinit var siteId: String
    private lateinit var exchangeName: String
    private lateinit var term: String

    private var selectedCryptoCurr: String = "Bitcoin"

    private lateinit var btnBtc: Button
    private lateinit var btnEth: Button
    private lateinit var btnLtc: Button

    private lateinit var btn1Hour: Button
    private lateinit var btn1Day: Button
    private lateinit var btn1Week: Button
    private lateinit var btn1Month: Button
    private lateinit var btnAll: Button

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view_main: View = inflater!!.inflate(R.layout.fragment_charts, container, false)

        lineChart = view_main.findViewById(R.id.price_chart)

        currencySpinner = view_main?.findViewById(R.id.currency_spinner)
        currencySpinner.onItemSelectedListener = this
        exchangeSpinner = view_main?.findViewById(R.id.exchange_spinner)
        exchangeSpinner.onItemSelectedListener = this

        updateCurrencyAndExchange("Bitcoin")
        siteId = "1"
        exchangeName = "Fyb-SG"
        term = "period=hour"

        History.draw(siteId, exchangeName, term, context, lineChart, "12345", "12345")

        btnBtc = view_main.findViewById(R.id.history_btc)
        btnBtc.setOnClickListener(this)
        btnEth = view_main.findViewById(R.id.history_eth)
        btnEth.setOnClickListener(this)
        btnLtc = view_main.findViewById(R.id.history_ltc)
        btnLtc.setOnClickListener(this)

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

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("Nothing", "Selected")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id){
            R.id.currency_spinner -> {
                updateExchange(selectedCryptoCurr, parent.selectedItem.toString())
            }
            R.id.exchange_spinner -> {
                Log.d("Click spin", listIdsForCurrentSettings[position])
                siteId = listIdsForCurrentSettings[position]
                exchangeName = parent.selectedItem.toString()
                History.draw(siteId, exchangeName, term,  context, lineChart, "12345", "12345")
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.history_btc -> {
                selectedCryptoCurr = "Bitcoin"
                var x = v as Button
                updateStyleCryptoButton(x)
                updateCurrencyAndExchange("Bitcoin")
            }
            R.id.history_eth -> {
                selectedCryptoCurr = "Ethereum"
                var x = v as Button
                updateStyleCryptoButton(x)
                updateCurrencyAndExchange("Ethereum")
            }
            R.id.history_ltc -> {
                selectedCryptoCurr = "Litecoin"
                var x = v as Button
                updateStyleCryptoButton(x)
                updateCurrencyAndExchange("Litecoin")
            }

            R.id.period_1_hour -> {
                term = "period=hour"
                var x = v as Button
                updateStyle(x)
                History.draw(siteId, exchangeName, term, context, lineChart, "12345", "12345")
            }
            R.id.period_1_day -> {
                term = "period=day"
                var x = v as Button
                updateStyle(x)
                History.draw(siteId, exchangeName, term, context, lineChart, "12345", "12345")
            }
            R.id.period_1_week -> {
                term = "period=week"
                var x = v as Button
                updateStyle(x)
                History.draw(siteId, exchangeName, term, context, lineChart, "12345", "12345")
            }
            R.id.period_1_month -> {
                term = "period=month"
                var x = v as Button
                updateStyle(x)
                History.draw(siteId, exchangeName, term, context, lineChart, "12345", "12345")
            }
            R.id.period_all -> {
                term = "period=all"
                var x = v as Button
                updateStyle(x)
                History.draw(siteId, exchangeName, term, context, lineChart, "12345", "12345")
            }
            else -> {

            }
        }
    }

    private fun updateExchange(cryptoCurr: String, curr: String) {val mDbHelper = ExchangeDetailsDbHelper(activity)

        val db = mDbHelper.readableDatabase

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        val projection = arrayOf(
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_ID,
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_EX_NAME,
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_CURRENCY)

// Filter results WHERE "title" = 'My Title'
        val selection = ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_CRYPTO_CURRENCY + " = ? AND " +
                                     ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_CURRENCY + " = ?"
        val selectionArgs = arrayOf(cryptoCurr, curr)

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
        var listIds: MutableList<String> = arrayListOf()
        var listNames: MutableList<String> = arrayListOf()
        var listExchangesForCurrentSettings: MutableList<String> = arrayListOf()
        listIdsForCurrentSettings = arrayListOf()
        while (cursor.moveToNext()){
            listIds.add(cursor.getInt(0).toString())
            listNames.add(cursor.getString(1))
        }
        listIdsForCurrentSettings.clear()
        for (i in 0 until listNames.size){
            listExchangesForCurrentSettings.add(listNames[i])
            listIdsForCurrentSettings.add(listIds[i])
        }
        var exchangeAdapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, listExchangesForCurrentSettings)
        exchangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exchangeSpinner.adapter = exchangeAdapter
    }

    private fun updateCurrencyAndExchange(cryptoCurr: String) {
        val mDbHelper = ExchangeDetailsDbHelper(activity)

        val db = mDbHelper.readableDatabase

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        val projection = arrayOf(
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_ID,
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_EX_NAME,
                ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_CURRENCY)

// Filter results WHERE "title" = 'My Title'
        val selection = ExchangeDetailsSchema.ExchangesDetailsEntry.COLUMN_NAME_CRYPTO_CURRENCY + " = ?"
        val selectionArgs = arrayOf(cryptoCurr)

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
        var listCurrencyAll: MutableList<String> = arrayListOf()
        var listCurrency: List<String> = arrayListOf()
        while (cursor.moveToNext()){
            listCurrencyAll.add(cursor.getString(2))
            listCurrency = listCurrencyAll.distinct()
        }
        var currencyAdapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, listCurrency)
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.adapter = currencyAdapter
        // Empty Spinner
        var exchangeAdapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, arrayListOf())
        exchangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exchangeSpinner.adapter = exchangeAdapter
    }

    private fun updateStyleCryptoButton(x: Button) {
        removeCurrentCryptoCurrStyle()
        x.background = ContextCompat.getDrawable(context, R.drawable.tag_currency_rounded)
        x.setTextColor(Color.WHITE)
    }

    private fun updateStyle(x: Button) {
        removeCurrentStyle()
        x.background = ContextCompat.getDrawable(context, R.drawable.tag_currency_rounded)
        x.setTextColor(Color.WHITE)
    }
    private fun removeCurrentCryptoCurrStyle() {
        btnBtc.background = ContextCompat.getDrawable(context, R.drawable.time_period_rounded)
        btnBtc.setTextColor(Color.BLACK)
        btnEth.background = ContextCompat.getDrawable(context, R.drawable.time_period_rounded)
        btnEth.setTextColor(Color.BLACK)
        btnLtc.background = ContextCompat.getDrawable(context, R.drawable.time_period_rounded)
        btnLtc.setTextColor(Color.BLACK)
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

