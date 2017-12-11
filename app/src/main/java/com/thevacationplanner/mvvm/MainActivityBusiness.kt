package com.thevacationplanner.mvvm

import com.thevacationplanner.dto.City
import com.thevacationplanner.dto.Forecast
import com.thevacationplanner.global.asString
import timber.log.Timber
import java.util.*

/**
 *Created by Anderson on 10/12/2017.
 */
class MainActivityBusiness {

    private var matchesFound = mutableListOf<Pair<Int, Int>>()

    fun getDestinationOptions(result: List<City>): MutableList<String> {
        val spinnerResult = result.flatMap { listOf(it.district) }.toMutableList()
        spinnerResult.add(0, "Choose a city")
        return spinnerResult
    }

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

        matchesFound.clear()

        test(fResult, 0, daysRequired)

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

    fun test(x: List<Int>, s: Int, dayRequired: Int) {
        if (s == x.size - 1)
            return

        var ns = s
        val y = mutableListOf<Int>()
        for (i in s until x.size - 1) {
            ns = i + 1
            if (x[i] > 0)
                y.add(x[i])
            else {
                break
            }
        }

        if (y.size >= dayRequired) {
            matchesFound.add(Pair(y.first(), y.last()))
        }

        test(x, ns, dayRequired)
    }
}