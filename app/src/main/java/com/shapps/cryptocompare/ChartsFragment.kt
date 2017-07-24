package com.shapps.cryptocompare

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

import com.google.android.gms.plus.PlusOneButton

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * [ChartsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ChartsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChartsFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    private var lineChart: LineChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_charts, container, false)

        var entries = ArrayList<Entry>()
        for (i in 1..25) {
            entries.add(Entry(i.toFloat(), i.toFloat()))
        }
        var lds = LineDataSet(entries, "India")
        lds.color = Color.parseColor("#003838")
        lds.valueTextColor = Color.parseColor("#bbb")

        var lineData = LineData(lds)


        // Implement this
        lineChart = view.findViewById<View>(R.id.price_chart) as LineChart
        lineChart.data

        return view
    }

    override fun onResume() {
        super.onResume()
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
}
