package com.thevacationplanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_run.setOnClickListener({ _ -> doStuff() })
    }

    private val wikiApiServe by lazy {
        WikiApiService.create()
    }

    private var disposable: Disposable? = null

    private fun doStuff() {

        val cities = wikiApiServe.getCities()

        disposable = cities
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> Timber.d(result[0].toString()) },
                        { t -> Timber.e(t) }
                )

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}
