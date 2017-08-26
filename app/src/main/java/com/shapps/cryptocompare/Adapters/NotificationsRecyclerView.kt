package com.shapps.cryptocompare.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.shapps.cryptocompare.MainFragments.Notifications.OnListFragmentInteractionListener
import com.shapps.cryptocompare.Model.NotificationContent.NotificationItem
import com.shapps.cryptocompare.R
import kotlinx.android.synthetic.main.notification_item.view.*

class NotificationsRecyclerView(private val mValues: List<NotificationItem>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<NotificationsRecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]

        holder.bindValues(mValues[position])

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem as NotificationItem)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: NotificationItem? = null

        fun bindValues(mValues: NotificationItem) {
//            mView.currency.text = mValues.currency
//            mView.site.text = mValues.site
            mView.value.text = mValues.value.toString()
            mView.type.text = mValues.type
        }
    }
}
