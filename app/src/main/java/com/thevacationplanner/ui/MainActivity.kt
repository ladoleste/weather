package com.thevacationplanner.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.thevacationplanner.R
import com.thevacationplanner.app.Constants.Companion.INTENT_WEATHER
import com.thevacationplanner.app.Constants.Companion.MIN_DAYS
import com.thevacationplanner.app.Constants.Companion.WEATHER_REQUEST_CODE
import com.thevacationplanner.dto.City
import com.thevacationplanner.dto.Forecast
import com.thevacationplanner.dto.Weather
import com.thevacationplanner.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel
    private var selectedWeather = arrayListOf<Weather>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

//        selectedWeather.add(Weather(0, "Clear"))
//        selectedWeather.add(Weather(0, "Partly Cloudy"))
//        selectedWeather.add(Weather(0, "Cold"))

        getCities()

        bt_weather.setOnClickListener({ _ ->
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra(INTENT_WEATHER, selectedWeather)
            startActivityForResult(intent, WEATHER_REQUEST_CODE)
        })

        bt_done.setOnClickListener({ _ -> done() })

        sp_cities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                viewModel.selectedCity = parent.selectedItem as City
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        Timber.d("started")
    }

    private fun done() {

        if (et_number_of_days.text.isBlank() || et_number_of_days.text.toString().toInt() < MIN_DAYS) {
            et_number_of_days.error = getString(R.string.inform_number_of_days, MIN_DAYS.toString())
            return
        }

        if (viewModel.selectedCity.woeid == 0) {
            Toast.makeText(this, getString(R.string.choose_destination), Toast.LENGTH_SHORT).show()
            sp_cities.performClick()
            return
        }

        getForecast()
    }

    private fun getForecast() {
        val toast = Toast.makeText(this, "Running results...", Toast.LENGTH_LONG)
        toast.show()

        val forecast = viewModel.getForecast(viewModel.selectedCity.woeid, Calendar.getInstance().get(Calendar.YEAR) + 1)

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

        tv_results.text =
                if (list != null && list.isNotEmpty())
                    viewModel.processResult(list, et_number_of_days.text.toString().toInt())
                else
                    "no matches found"

        Timber.d(tv_results.text.toString())
    }

    private fun applyFilters(result: List<Forecast>?): List<Forecast>? {
        val min = et_min.text.toString().toIntOrNull()
        val max = et_max.text.toString().toIntOrNull()

        val filter = result?.filter {
            (min == null || it.temperature.min >= min) && (max == null || it.temperature.max <= max)
        }?.toMutableList()

        if (selectedWeather.isNotEmpty()) {
            filter?.retainAll { selectedWeather.map { x -> x.name }.contains(it.weather) }
        }
        return filter?.toList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            selectedWeather = data?.getParcelableArrayListExtra<Weather>(INTENT_WEATHER) as ArrayList<Weather>
            var result = ""
            selectedWeather.forEach {
                result += it.name + ", "
            }

            tv_weather.text = if (result.isEmpty()) getString(R.string.doesn_t_matter) else result.dropLast(2)
        }
    }

    private fun getCities() {

        cDispose.add(viewModel.getDestinations()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            val spinnerResult = result.toMutableList()
                            spinnerResult.add(0, City(0, getString(R.string.choose_city)))
                            val adapter = ArrayAdapter(this, R.layout.item_spinner, spinnerResult)
                            sp_cities.adapter = adapter
                        },
                        { t -> Timber.e(t) }
                ))
    }
}
