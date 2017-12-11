package com.thevacationplanner.mvvm

import com.thevacationplanner.dto.Weather
import io.reactivex.Observable

/**
 *Created by Anderson on 10/12/2017.
 */
class WeatherListViewModel(private val mainRepository: MainRepository = MainRepository()) {

    fun getWeatherList(selectedWeather: Array<String>): Observable<List<Weather>> {
        return mainRepository.getWeatherList().doOnNext({ x ->

            for (w in x) {
                w.selected = selectedWeather.contains(w.name)
            }
        })
    }
}