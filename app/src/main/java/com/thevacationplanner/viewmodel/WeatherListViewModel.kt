package com.thevacationplanner.viewmodel

import android.arch.lifecycle.ViewModel
import com.thevacationplanner.app.MainRepository
import com.thevacationplanner.dto.Weather
import io.reactivex.Observable

/**
 *Created by Anderson on 10/12/2017.
 */
class WeatherListViewModel(private val mainRepository: MainRepository = MainRepository()) : ViewModel() {

    var selectedItems = arrayListOf<Weather>()
    private var weatherOptions: List<Weather>? = null

    fun getWeatherList(): Observable<List<Weather>> {
        return if (weatherOptions != null) Observable.just(weatherOptions) else mainRepository.getWeatherList().doOnNext({ x -> weatherOptions = x })
    }
}