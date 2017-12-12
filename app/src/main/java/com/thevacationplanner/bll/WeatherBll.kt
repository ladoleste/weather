package com.thevacationplanner.bll

import com.thevacationplanner.app.asString
import com.thevacationplanner.dto.Forecast
import timber.log.Timber
import java.util.*

/**
 *Created by Anderson on 10/12/2017.
 */
class WeatherBll {

    fun processResult(list: List<Forecast>?, daysRequired: Int): String {

        val fResult = mutableListOf<Int>()

        list?.forEachIndexed { index, forecast ->

            if (index < list.size - 2) {

                val current = Calendar.getInstance()
                current.time = forecast.date
                val cDayOfYear = current.get(Calendar.DAY_OF_YEAR)

                val next = Calendar.getInstance()
                next.time = list[index + 1].date
                val nDayOfYear = next.get(Calendar.DAY_OF_YEAR)

                fResult.add(cDayOfYear)

                if (cDayOfYear != nDayOfYear - 1) {
                    fResult.add(0)
                }
            }
        }

        val matchesFound = mutableListOf<Pair<Int, Int>>()

        findMatches(fResult, 0, daysRequired, matchesFound)

        var sResults = ""
        matchesFound.forEach { (a, b) ->

            val from = Calendar.getInstance()
            from.set(Calendar.DAY_OF_YEAR, a)

            val to = Calendar.getInstance()
            to.set(Calendar.DAY_OF_YEAR, b)

            Timber.d("FROM %s TO %s", from.time, to.time)
            sResults += String.format("\r\nFrom %s to %s", from.time.asString("EEE, MMM d"), to.time.asString("EEE, MMM d"))
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