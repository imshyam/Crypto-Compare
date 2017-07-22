package com.shapps.cryptocompare

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.shapps.cryptocompare.DashboardFragment.OnListFragmentInteractionListener
import com.shapps.cryptocompare.dummy.DummyContent.DummyItem
import kotlinx.android.synthetic.main.fragment_exchanges.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class ExchangesRecyclerViewAdapter(private val mValues: List<DummyItem>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<ExchangesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_exchanges, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]

        holder.bindValues(mValues[position])

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem as DummyItem)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        var mItem: DummyItem? = null

        fun bindValues(mValues: DummyItem) {
            mView.buy_value.text = mValues.id
            mView.sell_value.text = mValues.content
        }

    }
}
