package com.thevacationplanner

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.thevacationplanner.data.City
import com.thevacationplanner.data.Forecast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerResult: MutableList<String>
    private lateinit var selectedWeather: Array<String>

    private var disposableCities: Disposable? = null
    private var disposableForecast: Disposable? = null

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

        Timber.d("started")
    }

    private fun showResults() {
        val toast = Toast.makeText(this, "Running results...", Toast.LENGTH_SHORT)
        toast.show()

        val forecast = wikiApiServe.getForecast(455821, 2018)

        disposableForecast = forecast
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            toast.cancel()
                            doResult(result)
                        },
                        { t -> Timber.e(t) }
                )
    }

    private fun doResult(result: List<Forecast>?) {

        val min = et_min.text.toString().toInt()
        val max = et_max.text.toString().toInt()

        val r = result?.filter {
            selectedWeather.contains(it.weather) && it.temperature.min >= min && it.temperature.max <= max
            it.temperature.min >= 0 && it.temperature.max <= 50
        }

        val fResult = mutableListOf<Int>()

        r?.forEachIndexed { index, forecast ->

            if (index < r?.size - 2) {

                val current = Calendar.getInstance()
                current.time = forecast.date
                val cDayOfYear = current.get(Calendar.DAY_OF_YEAR)

                val next = Calendar.getInstance()
                next.time = r?.get(index + 1).date
                val nDayOfYear = next.get(Calendar.DAY_OF_YEAR)

                fResult.add(cDayOfYear)

                if (cDayOfYear != nDayOfYear - 1) {
                    fResult.add(0)
                }
            }
        }

        test(fResult, 0)

        lista.forEach { (a, b) ->

            val from = Calendar.getInstance()
            from.set(Calendar.DAY_OF_YEAR, a)

            val to = Calendar.getInstance()
            to.set(Calendar.DAY_OF_YEAR, b)

            Timber.d("FROM %s TO %s", from.time, to.time)
        }
    }

    private val lista = mutableListOf<Pair<Int, Int>>()

    fun test(x: List<Int>, s: Int) {
        if (s == x.size - 1)
            return

        var ns = s
        val y = mutableListOf<Int>()
        for (i in s until x.size - 1) {
            ns = i + 1
            if (x[i] > 0)
                y.add(x[i])
            else {
                break
            }
        }
        if (y.size > et_number_of_days.text.toString().toInt()) {
            lista.add(Pair(y.first(), y.last()))
        }

        test(x, ns)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        selectedWeather = data?.getStringArrayExtra("items") as Array<String>

        var result = ""
        selectedWeather.forEach {
            result += it + ", "
        }

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
