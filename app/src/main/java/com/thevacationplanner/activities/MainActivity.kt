package com.thevacationplanner.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.thevacationplanner.Constants.Companion.WEATHER_REQUEST_CODE
import com.thevacationplanner.R
import com.thevacationplanner.WeatherService
import com.thevacationplanner.asString
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
    private lateinit var resultCities: List<City>

    private val lista = mutableListOf<Pair<Int, Int>>()
    private var disposableCities: Disposable? = null
    private var disposableForecast: Disposable? = null
    private var selectedWeather = arrayOf<String>()

    private val wikiApiServe by lazy {
        WeatherService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getCities()

        bt_weather.setOnClickListener({ _ ->
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("selectedWeather", selectedWeather)
            startActivityForResult(intent, WEATHER_REQUEST_CODE)
        })
        bt_done.setOnClickListener({ _ -> done() })

        Timber.d("started")
    }

    private fun done() {
        if (sp_cities.selectedItemPosition == 0) {
            Toast.makeText(this, "Please choose a destination", Toast.LENGTH_SHORT).show()
            return
        }
        getData()
    }

    private fun getData() {
        val toast = Toast.makeText(this, "Running results...", Toast.LENGTH_LONG)
        toast.show()

        val forecast = wikiApiServe.getForecast(resultCities[sp_cities.selectedItemPosition - 1].woeid, 2018)

        disposableForecast = forecast
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            processResult(result)
                            toast.cancel()
                        },
                        { t -> Timber.e(t) }
                )
    }

    private fun processResult(result: List<Forecast>?) {

        val list = applyFilters(result)

        val fResult = mutableListOf<Int>()

        list?.forEachIndexed { index, forecast ->

            if (index < list.size - 2) {

                val current = Calendar.getInstance()
                current.time = forecast.date
                val cDayOfYear = current.get(Calendar.DAY_OF_YEAR)

                val next = Calendar.getInstance()
                next.time = list[index + 1].date
                val nDayOfYear = next.get(Calendar.DAY_OF_YEAR)

                fResult.add(cDayOfYear)

                if (cDayOfYear != nDayOfYear - 1) {
                    fResult.add(0)
                }
            }
        }

        lista.clear()

        test(fResult, 0)

        var sResults = ""
        lista.forEach { (a, b) ->

            val from = Calendar.getInstance()
            from.set(Calendar.DAY_OF_YEAR, a)

            val to = Calendar.getInstance()
            to.set(Calendar.DAY_OF_YEAR, b)

            Timber.d("FROM %s TO %s", from.time, to.time)
            sResults += String.format("\r\nFrom %s to %s", from.time.asString("EEE, MMM d"), to.time.asString("EEE, MMM d"))
        }

        tv_results.text = sResults
    }

    private fun applyFilters(result: List<Forecast>?): List<Forecast>? {
        val min = et_min.text.toString().toIntOrNull()
        val max = et_max.text.toString().toIntOrNull()

        val filter = result?.filter {
            (min == null || it.temperature.min >= min) && (max == null || it.temperature.max <= max)
        }?.toMutableList()

        if (selectedWeather.isNotEmpty()) {
            filter?.retainAll { selectedWeather.contains(it.weather) }
        }
        return filter?.toList()
    }

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

        if (resultCode == RESULT_OK) {
            selectedWeather = data?.getStringArrayExtra("items") as Array<String>

            var result = ""
            selectedWeather.forEach {
                result += it + ", "
            }

            tv_weather.text = if (result.isEmpty()) "Indiferente" else result.dropLast(2)
        }
    }

    private fun getCities() {

        val cities = wikiApiServe.getCities()

        disposableCities = cities
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            resultCities = result
                            fillSpinner(result)
                        },
                        { t -> Timber.e(t) }
                )
    }

    private fun fillSpinner(result: List<City>) {
        val districtResult = result.flatMap { listOf(it.district) }
        spinnerResult = districtResult.toMutableList()
        spinnerResult.add(0, "Choose a city")
        val adapter = ArrayAdapter(this, R.layout.item_spinner, spinnerResult)
        sp_cities.adapter = adapter
    }

    override fun onDestroy() {
        disposableCities?.dispose()
        super.onDestroy()
    }
}
