package com.shapps.cryptocompare.model

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object NotificationContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<NotificationItem> = ArrayList<NotificationItem>()

    /**
     * A map of sample (dummy) items, by ID.
     */
    private val ITEM_MAP: MutableMap<String, NotificationItem> = HashMap<String, NotificationItem>()

    private val COUNT = 25

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: NotificationItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): NotificationItem {
        return NotificationItem((position).toString(), "SGD " + position, "FYB-SG", 1231.25, "buy")
    }

    /**
     * A dummy item representing a piece of content.
     */
    class NotificationItem(val id: String, val currency: String, val site: String,
                                val value: Double, val type: String)
}
