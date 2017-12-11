package com.thevacationplanner.mvvm

import com.thevacationplanner.dto.City
import com.thevacationplanner.dto.Forecast
import io.reactivex.Observable

/**
 *Created by Anderson on 10/12/2017.
 */
class MainViewModel(private val mainRepository: MainRepository = MainRepository()) {

    fun getDestinations(): Observable<List<City>> {
        return mainRepository.getDestinations()
    }

    fun getForecast(id: Int, yeah: Int): Observable<List<Forecast>> {
        return mainRepository.getForecast(id, yeah)
    }
}