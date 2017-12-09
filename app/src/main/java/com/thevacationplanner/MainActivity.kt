package com.thevacationplanner

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var spinnerResult: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCities()
        bt_done.setOnClickListener({ _ -> Timber.d(sp_cities.selectedItemPosition.toString()) })
        bt_weather.setOnClickListener({ _ ->
            startActivity(Intent(this, Main2Activity::class.java))
        })
    }

    private val wikiApiServe by lazy {
        WeatherService.create()
    }

    private var disposable: Disposable? = null

    private fun getCities() {

        val cities = wikiApiServe.getCities()

        disposable = cities
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> fillSpinner(result) },
                        { t -> Timber.e(t) }
                )
    }

    private fun fillSpinner(result: List<City>) {
        val districtResult = result.flatMap { listOf(it.district) }
        spinnerResult = districtResult.toMutableList()
        spinnerResult.add(0, "Selecione")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerResult)
        sp_cities.adapter = adapter
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}
