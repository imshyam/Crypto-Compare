package com.shapps.cryptocompare.Networking

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.android.volley.RequestQueue



/**
 * Created by shyam on 27/8/17.
 */

class AppController : Application() {

    private var mRequestQueue: RequestQueue? = null
    private var mImageLoader: ImageLoader? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(getApplicationContext())
            }

            return this.mRequestQueue!!
        }

    val imageLoader: ImageLoader
        get() {
            requestQueue
            if (mImageLoader == null) {
                mImageLoader = ImageLoader(this.mRequestQueue,
                        LruBitmapCache())
            }
            return this.mImageLoader!!
        }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        // set the default tag if tag is empty
        req.setTag(if (TextUtils.isEmpty(tag)) TAG else tag)
        requestQueue.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.setTag(TAG)
        requestQueue.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    companion object {

        val TAG = AppController::class.java
                .simpleName

        @get:Synchronized
        var instance: AppController? = null
            private set
    }
}