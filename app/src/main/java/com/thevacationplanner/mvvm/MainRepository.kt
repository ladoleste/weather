package com.thevacationplanner.mvvm

import com.thevacationplanner.dto.City
import com.thevacationplanner.dto.Forecast
import com.thevacationplanner.dto.Weather
import io.reactivex.Observable

/**
 *Created by Anderson on 10/12/2017.
 */
class MainRepository(private val weatherApi: WeatherApi = WeatherApi.create()) {

    fun getDestinations(): Observable<List<City>> {
        return weatherApi.getCities()
    }

    fun getWeatherList(): Observable<List<Weather>> {
        return weatherApi.getWeatherList()
    }

    fun getForecast(id: Int, yeah: Int): Observable<List<Forecast>> {
        return weatherApi.getForecast(id, yeah)
    }

}