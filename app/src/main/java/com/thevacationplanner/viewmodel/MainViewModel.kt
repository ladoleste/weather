package com.thevacationplanner.viewmodel

import android.arch.lifecycle.ViewModel
import com.thevacationplanner.app.MainRepository
import com.thevacationplanner.bll.WeatherBll
import com.thevacationplanner.dto.City
import com.thevacationplanner.dto.Forecast
import com.thevacationplanner.dto.Weather
import io.reactivex.Observable
import java.util.*

/**
 *Created by Anderson on 10/12/2017.
 */
class MainViewModel(private val mainRepository: MainRepository = MainRepository()) : ViewModel() {

    var selectedCity = City()
    var destinations: List<City>? = null
    private var forecast: List<Forecast>? = null

    private val businessLogic = WeatherBll()

    fun getDestinations(): Observable<List<City>> {
        return if (destinations != null) Observable.just(destinations) else mainRepository.getDestinations().doOnNext({ x -> destinations = x })
    }

    fun getForecast(id: Int, yeah: Int): Observable<List<Forecast>> {
        return if (forecast != null) Observable.just(forecast) else mainRepository.getForecast(id, yeah).doOnNext({ x -> forecast = x })
    }

    fun processResult(list: List<Forecast>, daysRequired: Int): List<Pair<Date, Date>> {
        return businessLogic.processResult(list, daysRequired)
    }

    fun applyFilters(result: List<Forecast>?, min: Int?, max: Int?, selectedWeather: ArrayList<Weather>): List<Forecast>? {
        return businessLogic.applyFilters(result, min, max, selectedWeather)
    }
}