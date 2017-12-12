package com.thevacationplanner.viewmodel

import android.arch.lifecycle.ViewModel
import com.thevacationplanner.app.MainRepository
import com.thevacationplanner.dto.Weather
import io.reactivex.Observable

/**
 *Created by Anderson on 10/12/2017.
 */
class WeatherListViewModel(private val mainRepository: MainRepository = MainRepository()) : ViewModel() {

    var selectedItems = arrayListOf<String>()

    fun getWeatherList(): Observable<List<Weather>> {
        return mainRepository.getWeatherList().doOnNext({ x ->
            for (w in x) {
                w.selected = selectedItems.contains(w.name)
            }
        })
    }
}