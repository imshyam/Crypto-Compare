package com.shapps.cryptocompare.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.shapps.cryptocompare.Fragments.Dashboard.OnListFragmentInteractionListener
import com.shapps.cryptocompare.Model.LiveDataContent.LiveData
import com.shapps.cryptocompare.R
import kotlinx.android.synthetic.main.card_exchanges.view.*


/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class ExchangesRecyclerView(private val mValues: List<LiveData>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<ExchangesRecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_exchanges, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]

        holder.bindValues(mValues[position])

        holder.mView.findViewById<LinearLayout>(R.id.details_layout).setOnClickListener {
            mListener?.onListFragmentInteraction(mValues[position].cryptoCurrency, mValues[position].currency,
                    mValues[position].exchangeId, mValues[position].exchangeName, mValues[position].priceBuy, mValues[position].priceSell,
                    mValues[position].lowBuy, mValues[position].highBuy, mValues[position].lowSell, mValues[position].highSell)
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        var mItem: LiveData? = null

        fun bindValues(mValues: LiveData) {
            mView.exchange_name.text = mValues.exchangeName
            mView.currency_name.text = mValues.currency
            mView.buy_value.text = mValues.priceBuy
            mView.sell_value.text = mValues.priceSell
            mView.volume.text = mValues.volume
            mView.low_buy.text = mValues.lowBuy
            mView.high_buy.text = mValues.highBuy
            mView.low_sell.text = mValues.lowSell
            mView.high_sell.text = mValues.highSell
            if(mValues.cryptoCurrency == "Ethereum")
                mView.currency_icon.setImageResource(R.drawable.ic_ethereum)
        }

    }
}
