package com.thevacationplanner.mvvm

import com.thevacationplanner.dto.City
import com.thevacationplanner.dto.Forecast
import io.reactivex.Observable

/**
 *Created by Anderson on 10/12/2017.
 */
class MainViewModel(private val mainRepository: MainRepository = MainRepository()) {

    private val businessLogic = MainActivityBusiness()

    fun getDestinations(): Observable<List<City>> {
        return mainRepository.getDestinations()
    }

    fun getForecast(id: Int, yeah: Int): Observable<List<Forecast>> {
        return mainRepository.getForecast(id, yeah)
    }

    fun processResult(list: List<Forecast>, daysRequired: Int): String {
        return businessLogic.processResult(list, daysRequired)
    }

    fun getDestinationOptions(result: List<City>): List<String> {
        return businessLogic.getDestinationOptions(result)
    }
}