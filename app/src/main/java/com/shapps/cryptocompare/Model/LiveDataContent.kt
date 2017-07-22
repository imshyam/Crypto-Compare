package com.shapps.cryptocompare.Model

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by shyam on 23/7/17.
 */

object LiveDataContent {

    /**
     * An array of sample items.
     */
    val ITEMS: MutableList<LiveData> = ArrayList()

    /**
     * A map of sample items, by ID.
     */
    val ITEM_MAP: MutableMap<String, LiveData> = HashMap()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createLiveData(i))
        }
    }

    private fun addItem(item: LiveData) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createLiveData(position: Int): LiveData {
        return LiveData(position.toString(), "Fyb-SG " + position, "SGD",
                "1239.32", "2321.22", "5 mins ago")
    }

    class LiveData(val id: String, val exchangeName: String, val currencyCode: String,
                   val priceBuy: String, val priceSell: String, val lastUpdated: String) {

        override fun toString(): String {
            return exchangeName
        }

    }
}
