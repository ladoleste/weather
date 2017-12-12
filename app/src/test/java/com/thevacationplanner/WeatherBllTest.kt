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
    fun processPortoAlegre() {
        val portoAlegre: List<Forecast> = Gson().fromJson(Source().portoAlegre, object : TypeToken<List<Forecast>>() {}.type)

        val selectedWeather = arrayListOf<Weather>()
        selectedWeather.add(Weather(0, "clear"))
        selectedWeather.add(Weather(0, "partly cloudy"))
        selectedWeather.add(Weather(0, "cold"))

        val bll = WeatherBll()

        val results = bll.processResult(bll.applyFilters(portoAlegre, null, null, selectedWeather), 15)

        Assert.assertEquals(3, results.size)
        Assert.assertEquals("2 de Abril", results[0].first.asString())
        Assert.assertEquals("17 de Abril", results[0].second.asString())

        Assert.assertEquals(results[1].first.asString(), "9 de Maio")
        Assert.assertEquals("26 de Maio", results[1].second.asString())

        Assert.assertEquals("20 de Junho", results[2].first.asString())
        Assert.assertEquals("10 de Julho", results[2].second.asString())
    }

    @Test
    fun processRioDeJaneiro() {
        val rioDeJaneiro: List<Forecast> = Gson().fromJson(Source().rioDeJaneiro, object : TypeToken<List<Forecast>>() {}.type)

        val selectedWeather = arrayListOf<Weather>()
        selectedWeather.add(Weather(0, "clear"))
        selectedWeather.add(Weather(0, "partly cloudy"))
        selectedWeather.add(Weather(0, "hot"))
        selectedWeather.add(Weather(0, "fair"))

        val bll = WeatherBll()

        val results = bll.processResult(bll.applyFilters(rioDeJaneiro, null, null, selectedWeather), 15)

        Assert.assertEquals(1, results.size)
        Assert.assertEquals("10 de Novembro", results[0].first.asString())
        Assert.assertEquals("1 de Dezembro", results[0].second.asString())
    }

    @Test
    fun process_temp() {
        val rioDeJaneiro: List<Forecast> = Gson().fromJson(Source().rioDeJaneiro, object : TypeToken<List<Forecast>>() {}.type)

        val selectedWeather = arrayListOf<Weather>()

        val bll = WeatherBll()

        val results = bll.processResult(bll.applyFilters(rioDeJaneiro, null, null, selectedWeather), 15)

        Assert.assertEquals(1, results.size)
        Assert.assertEquals("1 de Janeiro", results[0].first.asString())
        Assert.assertEquals("31 de Dezembro", results[0].second.asString())
    }
}