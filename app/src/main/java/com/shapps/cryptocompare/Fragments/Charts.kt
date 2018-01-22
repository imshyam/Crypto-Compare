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
class Charts : Fragment(), View.OnClickListener {

    private var mListener: OnFragmentInteractionListener? = null

    private lateinit var lineChart: LineChart

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
        History.draw("1", "Fyb-Sg", "hours=1", context, lineChart, "12345", "12345")

        // Currency
        val spinner = view_main?.findViewById<Spinner>(R.id.currency_spinner)
        var adapter = ArrayAdapter.createFromResource(activity, R.array.currency_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        if (spinner != null) {
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Changed", position.toString() + " and " + id.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        // Exchange
        val exchangeSpinner = view_main?.findViewById<Spinner>(R.id.exchange_spinner)
        var adapter2 = ArrayAdapter.createFromResource(activity, R.array.exchange_list_sgd, android.R.layout.simple_spinner_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        if (exchangeSpinner != null) {
            exchangeSpinner.adapter = adapter2
        }
        exchangeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var exId: String = resources.getStringArray(R.array.exchange_id_sgd)[position]
                Log.d("Changed", exId)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
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

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.history_btc -> {
                var x = v as Button
                updateStyleCryptoButton(x)
            }
            R.id.history_eth -> {
                var x = v as Button
                updateStyleCryptoButton(x)
                History.draw("1", "Fyb-Sg", "days=100", context, lineChart, "12345", "12345")
            }
            R.id.history_ltc -> {
                var x = v as Button
                updateStyleCryptoButton(x)
                History.draw("1", "Fyb-Sg", "days=100", context, lineChart, "12345", "12345")
            }

            R.id.period_1_hour -> {
                var x = v as Button
                updateStyle(x)
                History.draw("1", "Fyb-Sg", "hours=1", context, lineChart, "12345", "12345")
            }
            R.id.period_1_day -> {
                var x = v as Button
                updateStyle(x)
                History.draw("1", "Fyb-Sg", "days=1", context, lineChart, "12345", "12345")
            }
            R.id.period_1_week -> {
                var x = v as Button
                updateStyle(x)
                History.draw("1", "Fyb-Sg", "days=7", context, lineChart, "12345", "12345")
            }
            R.id.period_1_month -> {
                var x = v as Button
                updateStyle(x)
                History.draw("1", "Fyb-Sg", "days=30", context, lineChart, "12345", "12345")
            }
            R.id.period_all -> {
                var x = v as Button
                updateStyle(x)
                History.draw("1", "Fyb-Sg", "days=100", context, lineChart, "12345", "12345")
            }
            else -> {

            }
        }
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

