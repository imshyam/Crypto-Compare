package com.shapps.cryptocompare.Constants

import android.util.Log

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by shyam on 28/10/17.
 */

class Exchanges {


    companion object {

        fun saveData(json: String?) {
            Log.d("Ex : ", json)
            try {
                var jsonArr = JSONArray(json)

                (0 until jsonArr.length())
                        .map { JSONObject(jsonArr.get(it).toString()) }
                        .forEach {
                            if (it.getString("crypto_currency").equals("Bitcoin")) {
                                var currency = it.getString("currency")
                                Log.d("Exchange", it.getString("name"))
                            }
                        }

            }
            catch (ex: Exception){
            }
        }
    }
}
