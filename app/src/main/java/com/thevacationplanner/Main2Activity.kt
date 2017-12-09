package com.thevacationplanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main2.*
import timber.log.Timber


class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        getWeather()
//        bt_done.setOnClickListener({ _ -> Timber.d(sp_cities.selectedItemPosition.toString()) })
    }

    private val wikiApiServe by lazy {
        WeatherService.create()
    }

    private var disposable: Disposable? = null

    private fun getWeather() {

        val weather = wikiApiServe.getWeather()

        disposable = weather
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> rv_list.adapter = WeatherAdapter(result) },
                        { t -> Timber.e(t) }
                )
    }


    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}
