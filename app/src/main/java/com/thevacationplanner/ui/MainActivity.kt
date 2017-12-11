package com.thevacationplanner.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.thevacationplanner.R
import com.thevacationplanner.dto.City
import com.thevacationplanner.dto.Forecast
import com.thevacationplanner.global.Constants.Companion.WEATHER_REQUEST_CODE
import com.thevacationplanner.global.asString
import com.thevacationplanner.mvvm.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var spinnerResult: MutableList<String>
    private lateinit var resultCities: List<City>

    private val lista = mutableListOf<Pair<Int, Int>>()
    private var selectedWeather = arrayOf<String>()
    private val viewModel = MainViewModel()

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

        if (et_number_of_days.text.isBlank() || et_number_of_days.text.toString().toInt() < 3) {
            et_number_of_days.error = getString(R.string.inform_number_of_days)
            return
        }

        if (sp_cities.selectedItemPosition == 0) {
            Toast.makeText(this, getString(R.string.choose_destination), Toast.LENGTH_SHORT).show()
            sp_cities.performClick()
            return
        }

        getForecast()
    }

    private fun getForecast() {
        val toast = Toast.makeText(this, "Running results...", Toast.LENGTH_LONG)
        toast.show()

        val forecast = viewModel.getForecast(resultCities[sp_cities.selectedItemPosition - 1].woeid, Calendar.getInstance().get(Calendar.YEAR) + 1)

        cDispose.add(forecast
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            processResult(result)
                            toast.cancel()
                        },
                        { t -> Timber.e(t) }
                ))
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

            tv_weather.text = if (result.isEmpty()) getString(R.string.it_doesn_t_matter) else result.dropLast(2)
        }
    }

    private fun getCities() {

        cDispose.add(viewModel.getDestinations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            resultCities = result
                            fillSpinner(result)
                        },
                        { t -> Timber.e(t) }
                ))
    }

    private fun fillSpinner(result: List<City>) {
        spinnerResult = result.flatMap { listOf(it.district) }.toMutableList()
        spinnerResult.add(0, "Choose a city")
        val adapter = ArrayAdapter(this, R.layout.item_spinner, spinnerResult)
        sp_cities.adapter = adapter
    }

}
