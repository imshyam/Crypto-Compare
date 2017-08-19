package com.shapps.cryptocompare

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_add_notification.*
import java.util.*
import android.widget.Toast



class AddNotificationActivity : AppCompatActivity(), OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notification)
        setupActionBar()

        val spinnerArray = ArrayList<CharSequence>()
        spinnerArray.add("Crypto Currency")
        spinnerArray.add("Bitcoin")
        spinnerArray.add("Ethereum")
        spinnerArray.add("Litecoin")
        val cryptoCurrencyArray = ArrayAdapter(
                this, android.R.layout.simple_spinner_item, spinnerArray)
        cryptoCurrencyArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        spinner_crypto_currency.adapter = cryptoCurrencyArray


        val spinnerArray1 = ArrayList<String>()
        spinnerArray1.add("Currency")
        spinnerArray1.add("SGD")
        spinnerArray1.add("INR")
        spinnerArray1.add("USD")
        spinnerArray1.add("EUR")
        val currencyArray = ArrayAdapter(
                this, android.R.layout.simple_spinner_item, spinnerArray1)
        currencyArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_currency.adapter = currencyArray


        val spinnerArray2 = ArrayList<String>()
        spinnerArray2.add("Exchange")
        spinnerArray2.add("FYB-SG")
        spinnerArray2.add("zebpay")
        spinnerArray2.add("coinbase")
        val exchangeArray = ArrayAdapter(
                this, android.R.layout.simple_spinner_item, spinnerArray2)
        exchangeArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_exchange.adapter = exchangeArray

        spinner_crypto_currency.onItemSelectedListener = this
        spinner_currency.onItemSelectedListener = this
        spinner_exchange.onItemSelectedListener = this

        price_picker.maxValue = 20
        price_picker.minValue = 0
        price_picker.value = 5

    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
        Log.d("Selected : ", parent?.getItemAtPosition(pos).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d("Nothing ", "Selected")
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.finish()
        }
        return true
    }
}
