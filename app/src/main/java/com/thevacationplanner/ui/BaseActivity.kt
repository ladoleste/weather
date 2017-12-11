package com.thevacationplanner.ui

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

/**
 *Created by Anderson on 10/12/2017.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    val cDispose = CompositeDisposable()

    override fun onDestroy() {
        cDispose.clear()
        super.onDestroy()
    }
}