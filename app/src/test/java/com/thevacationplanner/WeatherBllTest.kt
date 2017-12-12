package com.thevacationplanner

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thevacationplanner.app.asString
import com.thevacationplanner.bll.WeatherBll
import com.thevacationplanner.dto.Forecast
import com.thevacationplanner.dto.Weather
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WeatherBllTest {
    @Test
    fun processResult() {
        val portoAlegre: List<Forecast> = Gson().fromJson(Source().portoAlegre, object : TypeToken<List<Forecast>>() {}.type)

        val selectedWeather = arrayListOf<Weather>()
        selectedWeather.add(Weather(0, "clear"))
        selectedWeather.add(Weather(0, "partly cloudy"))
        selectedWeather.add(Weather(0, "cold"))

        val bll = WeatherBll()

        val results = bll.processResult(bll.applyFilters(portoAlegre, null, null, selectedWeather), 15)

        Assert.assertTrue(results.size == 3)
        Assert.assertEquals(results[0].first.asString(), "2 de Abril")
        Assert.assertEquals(results[0].second.asString(), "17 de Abril")

        Assert.assertEquals(results[1].first.asString(), "9 de Maio")
        Assert.assertEquals(results[1].second.asString(), "26 de Maio")

        Assert.assertEquals(results[2].first.asString(), "20 de Junho")
        Assert.assertEquals(results[2].second.asString(), "10 de Julho")
    }
}