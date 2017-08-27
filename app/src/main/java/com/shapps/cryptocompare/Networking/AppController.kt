package com.shapps.cryptocompare.Networking

import android.app.Application
import android.content.Context
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
    private var mInstance: AppController? = null
    private var contxt: Context? = null

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    private val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(contxt)
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

    fun <T> addToRequestQueue(req: Request<T>, tag: String, context: Context) {
        // set the default tag if tag is empty
        this.contxt = context
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        requestQueue.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    companion object {

        val TAG = AppController::class.java
                .simpleName!!

        var instance: AppController? = AppController()
            private set
    }
}