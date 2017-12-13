package com.thevacationplanner.app

/**
 *Created by Anderson on 13/12/2017.
 */
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient

object OkHttpProvider {
    private var instance: OkHttpClient? = null

    val okHttpInstance: OkHttpClient
        get() {
            if (instance == null) {
                instance = OkHttpClient.Builder()
                        .addNetworkInterceptor(StethoInterceptor())
                        .build()
            }
            return instance as OkHttpClient
        }
}