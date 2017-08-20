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


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var cryptoCurrency: String? = null
    private var currency: String? = null
    private var siteId: Int? = null
    private var siteName: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            cryptoCurrency = arguments.getString(CRYPTO_CURRENCY)
            currency = arguments.getString(CURRENCY)
            siteId = arguments.getString(SITE_ID).toInt()
            siteName = arguments.getString(SITE_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_details, container, false)
        var entries = ArrayList<Entry>()
        for (i in 1..25) {
            entries.add(Entry(i.toFloat(), i.toFloat()))
        }
        var lds = LineDataSet(entries, "India")
        lds.color = Color.parseColor("#003838")
        lds.valueTextColor = Color.parseColor("#bbbbbb")

        var lineData = LineData(lds)


        // Implement this
        val lineChart = view.findViewById<View>(R.id.exchange_chart) as LineChart
        lineChart.data = lineData
        lineChart.invalidate()

        return view
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
        private val CRYPTO_CURRENCY = "Bitcoin"
        private val CURRENCY = "SGD"
        private val SITE_ID = "1"
        private val SITE_NAME = "FYB-SG"


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(cryptoCurr: String, curr: String, siteId: String, siteName: String): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putString(CRYPTO_CURRENCY, cryptoCurr)
            args.putString(CURRENCY, curr)
            args.putString(SITE_ID, siteId)
            args.putString(SITE_NAME, siteName)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
