package com.thevacationplanner.ui

import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.thevacationplanner.R
import com.thevacationplanner.app.Constants.Companion.INTENT_WEATHER
import com.thevacationplanner.app.Constants.Companion.MIN_DAYS
import com.thevacationplanner.app.Constants.Companion.WEATHER_REQUEST_CODE
import com.thevacationplanner.app.asString
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

        getCities()

        bt_weather.setOnClickListener({ _ ->
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra(INTENT_WEATHER, selectedWeather)
            startActivityForResult(intent, WEATHER_REQUEST_CODE)
        })

        bt_done.setOnClickListener({ _ -> done() })

        bt_cities.setOnClickListener({ _ ->
            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.choose_city))
                    .setPositiveButton(R.string.cancel, null)
                    .setItems(viewModel.destinations?.map { it.district }?.toTypedArray(), { _, which ->
                        viewModel.selectedCity = viewModel.destinations!![which]
                        tv_selected_city.text = viewModel.selectedCity.district
                    })
                    .create().show()
        })

        bt_clear.setOnClickListener({ _ ->
            et_max.text.clear()
            et_min.text.clear()
            et_number_of_days.text.clear()
            viewModel.selectedCity = City()
            selectedWeather.clear()
            tv_weather.text = ""
        })

        Timber.d("started")
    }

    private fun done() {

        if (et_number_of_days.text.isBlank() || et_number_of_days.text.toString().toInt() < MIN_DAYS) {
            et_number_of_days.error = getString(R.string.inform_number_of_days, MIN_DAYS.toString())
            return
        }

        if (viewModel.selectedCity.woeid == 0) {
            Snackbar.make(root_view, R.string.choose_destination, Snackbar.LENGTH_LONG).show()
            bt_cities.performClick()
            return
        }

        getForecast()
    }

    private fun getForecast() {
        val forecast = viewModel.getForecast(viewModel.selectedCity.woeid, Calendar.getInstance().get(Calendar.YEAR) + 1)

        cDispose.add(forecast
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            processResult(result)
                        },
                        { t ->
                            Timber.e(t)
                            Snackbar.make(root_view, R.string.error, Snackbar.LENGTH_LONG).show()
                        }
                ))
    }

    private fun processResult(list: List<Forecast>?) {
        val min = et_min.text.toString().toIntOrNull()
        val max = et_max.text.toString().toIntOrNull()

        val filteredList = viewModel.applyFilters(list, min, max, selectedWeather)

        if (filteredList != null && filteredList.isNotEmpty()) {
            val result = viewModel.processResult(filteredList, et_number_of_days.text.toString().toInt())

            if (result.isEmpty()) {
                Snackbar.make(root_view, R.string.no_matches, Snackbar.LENGTH_LONG).show()

                return
            }

            val result2 = mutableListOf<String>()

            result.forEach {
                result2.add(String.format(getString(R.string.from_to), it.first.asString(), it.second.asString()))
            }

            Timber.d(result2.toString())

            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.your_results))
                    .setItems(result2.toTypedArray(), null)
                    .setPositiveButton("OK", null)
                    .create().show()
        } else {
            Snackbar.make(root_view, R.string.no_matches, Snackbar.LENGTH_LONG).show()
        }

        Timber.d(tv_results.text.toString())
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
                .subscribe({
                    bt_cities.isEnabled = true
                }, { t ->
                    Timber.e(t)
                    Snackbar.make(root_view, R.string.error, Snackbar.LENGTH_LONG).show()
                }))
    }
}
