package com.thevacationplanner.app

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

/**
 *Created by Anderson on 08/12/2017.
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //todo needs a version for production
        Stetho.initializeWithDefaults(this)
        Timber.plant(SuperLog())
    }
}