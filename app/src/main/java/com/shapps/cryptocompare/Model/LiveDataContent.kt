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
    private val ITEM_MAP: MutableMap<String, LiveData> = HashMap()

    init {
        // Add some sample items.
    }

    fun addItem(item: LiveData) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    class LiveData(val id: String, val cryptoCurrency: String, val currency: String, val exchangeId: String, val exchangeName: String,
                   val priceBuy: String, val priceSell: String, val volume: String, val lowBuy: String, val highBuy: String,
                   val lowSell: String, val highSell: String)

    fun dumpData() {
        ITEMS.clear()
        ITEM_MAP.clear()
    }
}
