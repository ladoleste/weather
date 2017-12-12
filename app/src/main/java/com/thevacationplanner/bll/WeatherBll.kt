package com.thevacationplanner.bll

import com.thevacationplanner.dto.Forecast
import com.thevacationplanner.dto.Weather
import timber.log.Timber
import java.util.*

/**
 *Created by Anderson on 10/12/2017.
 */
class WeatherBll {

    fun applyFilters(list: List<Forecast>?, min: Int?, max: Int?, selectedWeather: List<Weather>): List<Forecast>? {
        val filter = list?.filter {
            (min == null || it.temperature.min >= min) && (max == null || it.temperature.max <= max)
        }?.toMutableList()

        if (selectedWeather.isNotEmpty()) {
            filter?.retainAll { selectedWeather.map { x -> x.name }.contains(it.weather) }
        }
        return filter?.toList()
    }

    fun processResult(list: List<Forecast>?, daysRequired: Int): List<Pair<Date, Date>> {

        val fResult = mutableListOf<Int>()

        list?.forEachIndexed { index, forecast ->

            if (index < list.size - 1) {

                val current = Calendar.getInstance()
                current.time = forecast.date
                val cDayOfYear = current.get(Calendar.DAY_OF_YEAR)

                val next = Calendar.getInstance()
                next.time = list[index + 1].date
                val nDayOfYear = next.get(Calendar.DAY_OF_YEAR)

                if (cDayOfYear == nDayOfYear - 1) {
                    fResult.add(cDayOfYear)
                } else {
                    fResult.add(cDayOfYear)
                    fResult.add(0)//break the sequence
                }
            } else {
                //it's safe to just add the last one
                val current = Calendar.getInstance()
                current.time = forecast.date
                val cDayOfYear = current.get(Calendar.DAY_OF_YEAR)
                fResult.add(cDayOfYear)
            }
        }

        fResult.add(0)//break the sequence

        val matchesFound = mutableListOf<Pair<Int, Int>>()

        findMatches(fResult, 0, daysRequired, matchesFound)

        val sResults = mutableListOf<Pair<Date, Date>>()
        matchesFound.forEach { (a, b) ->

            val from = Calendar.getInstance()
            from.set(Calendar.DAY_OF_YEAR, a)

            val to = Calendar.getInstance()
            to.set(Calendar.DAY_OF_YEAR, b)

            Timber.d("FROM %s TO %s", from.time, to.time)
            sResults.add(from.time to to.time)
        }

        return sResults
    }

    private fun findMatches(source: List<Int>, index: Int, daysRequired: Int, matchesFound: MutableList<Pair<Int, Int>>) {
        if (index == source.size - 1)
            return

        var newIndex = index
        val tempResult = mutableListOf<Int>()
        for (i in index until source.size - 1) {
            newIndex = i + 1
            if (source[i] > 0)
                tempResult.add(source[i])
            else {
                break
            }
        }

        if (tempResult.size >= daysRequired) {
            matchesFound.add(Pair(tempResult.first(), tempResult.last()))
        }

        return findMatches(source, newIndex, daysRequired, matchesFound)
    }
}