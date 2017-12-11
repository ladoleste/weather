package com.thevacationplanner.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.thevacationplanner.R
import com.thevacationplanner.mvvm.WeatherListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_weather.*
import timber.log.Timber

class WeatherActivity : BaseActivity() {

    private lateinit var weatherAdapter: WeatherAdapter
    private val weatherViewModel = WeatherListViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        weatherAdapter = WeatherAdapter()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getWeather()
    }
    private fun getWeather() {

        val selectedWeather = intent.extras.getStringArray("selectedWeather")

        val weatherList = weatherViewModel.getWeatherList(selectedWeather)

        cDispose.add(weatherList
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            rv_list.adapter = weatherAdapter
                            weatherAdapter.setItems(result)
                        },
                        { t -> Timber.e(t) }
                ))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.weather, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.item_done) {
            val i = Intent(this, WeatherActivity::class.java)
            val selectedItems = weatherAdapter.getSelectedItems()
            i.putExtra("items", selectedItems.toTypedArray())
            setResult(Activity.RESULT_OK, i)
        }

        finish()

        return super.onOptionsItemSelected(item)
    }
}
