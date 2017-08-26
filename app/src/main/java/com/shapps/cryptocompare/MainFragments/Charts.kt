package com.shapps.cryptocompare.MainFragments

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
import com.shapps.cryptocompare.R

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
        lds.valueTextColor = Color.parseColor("#bbbbbb")

        var lineData = LineData(lds)


        // Implement this
        val lineChart = view.findViewById<View>(R.id.price_chart) as LineChart
        lineChart.data = lineData
        lineChart.invalidate()

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

