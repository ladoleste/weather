package com.thevacationplanner.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.thevacationplanner.R
import com.thevacationplanner.app.Constants.Companion.MIN_DAYS
import com.thevacationplanner.app.Constants.Companion.WEATHER_REQUEST_CODE
import com.thevacationplanner.dto.City
import com.thevacationplanner.dto.Forecast
import com.thevacationplanner.ui.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

class MainActivity : BaseActivity() {

    private var selectedWeather = arrayOf<String>()
    private val viewModel = MainViewModel()
    private var resultCities = listOf<City>()

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

        if (et_number_of_days.text.isBlank() || et_number_of_days.text.toString().toInt() < MIN_DAYS) {
            et_number_of_days.error = getString(R.string.inform_number_of_days, MIN_DAYS.toString())
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

        tv_results.text =
                if (list != null && list.isNotEmpty())
                    viewModel.processResult(list, et_number_of_days.text.toString().toInt())
                else
                    "no matches found"
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
                            val adapter = ArrayAdapter(this, R.layout.item_spinner, viewModel.getDestinationOptions(result))
                            sp_cities.adapter = adapter
                        },
                        { t -> Timber.e(t) }
                ))
    }
}
