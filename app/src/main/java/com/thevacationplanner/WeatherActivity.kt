package com.thevacationplanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_weather.*
import timber.log.Timber

class WeatherActivity : AppCompatActivity() {

    private lateinit var weatherAdapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        weatherAdapter = WeatherAdapter()
        getWeather()
    }

    private val wikiApiServe by lazy {
        WeatherService.create()
    }

    private var disposable: Disposable? = null

    private fun getWeather() {

        val weather = wikiApiServe.getWeather()

        disposable = weather
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            rv_list.adapter = weatherAdapter
                            weatherAdapter.setItems(result)
                        },
                        { t -> Timber.e(t) }
                )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.weather, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val i = Intent(this, WeatherActivity::class.java)
        val selectedItems = weatherAdapter.getSelectedItems()
        i.putExtra("items", selectedItems.toTypedArray())
        setResult(Activity.RESULT_OK, i)
        finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}
