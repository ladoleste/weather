package com.thevacationplanner.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import com.thevacationplanner.R
import com.thevacationplanner.app.Constants.Companion.INTENT_WEATHER
import com.thevacationplanner.viewmodel.WeatherListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_weather.*
import timber.log.Timber

class WeatherActivity : BaseActivity() {

    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var viewModel: WeatherListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        viewModel = ViewModelProviders.of(this).get(WeatherListViewModel::class.java)

        if (viewModel.selectedItems.isEmpty()) {
            viewModel.selectedItems = intent.getParcelableArrayListExtra(INTENT_WEATHER)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getWeather()
    }

    private fun getWeather() {

        val weatherList = viewModel.getWeatherList()

        cDispose.add(weatherList
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            weatherAdapter = WeatherAdapter(result, viewModel.selectedItems)
                            rv_list.adapter = weatherAdapter
                        },
                        { t ->
                            Timber.e(t)
                            Snackbar.make(root_view, R.string.error, Snackbar.LENGTH_LONG).show()
                        }
                ))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.weather, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.item_done) {
            val intent = Intent()
            intent.putParcelableArrayListExtra(INTENT_WEATHER, viewModel.selectedItems)
            setResult(Activity.RESULT_OK, intent)
        }

        finish()

        return super.onOptionsItemSelected(item)
    }
}
