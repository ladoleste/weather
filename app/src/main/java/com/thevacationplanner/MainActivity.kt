package com.thevacationplanner

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.thevacationplanner.data.City
import com.thevacationplanner.data.Forecast
import com.thevacationplanner.data.Weather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerResult: MutableList<String>
    private var disposableCities: Disposable? = null
    private var disposableOther: Disposable? = null

    private val wikiApiServe by lazy {
        WeatherService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCities()
        bt_done.setOnClickListener({ _ -> Timber.d(sp_cities.selectedItemPosition.toString()) })
        bt_weather.setOnClickListener({ _ ->
            startActivityForResult(Intent(this, WeatherActivity::class.java), 1)
        })
        bt_done.setOnClickListener({ _ -> showResults() })
    }

    private fun showResults() {
        val toast = Toast.makeText(this, "Running results...", Toast.LENGTH_SHORT)
        toast.show()

        val forecast = wikiApiServe.getForecast()

        disposableCities = forecast
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> doResult(result) },
                        { t -> Timber.e(t) }
                )
    }

    private fun doResult(result: List<Forecast>?) {
        result.filter { it. }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val items = data?.getParcelableArrayListExtra<Weather>("items")
        var result = ""
        items?.forEach { result += it.name + ", " }

        tv_weather.text = if (result.isEmpty()) "nenhum item selecionado" else result.dropLast(2)
    }

    private fun getCities() {

        val cities = wikiApiServe.getCities()

        disposableCities = cities
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
        disposableCities?.dispose()
        super.onDestroy()
    }
}
