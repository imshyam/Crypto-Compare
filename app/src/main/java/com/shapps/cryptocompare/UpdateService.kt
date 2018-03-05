package com.shapps.cryptocompare

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.shapps.cryptocompare.fragments.Dashboard

/**
 * Created by shyam on 2/3/18.
 */
class UpdateService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private lateinit var mContext: Context
    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action.equals(Dashboard.ACTION_REFRESH)){
            Log.d("log", "Apple")
        }
        else if(intent?.action.equals(Dashboard.ACTION_OPEN_APP)) {
            Log.d("Something", "else")
        }
        return super.onStartCommand(intent, flags, startId)
    }
}