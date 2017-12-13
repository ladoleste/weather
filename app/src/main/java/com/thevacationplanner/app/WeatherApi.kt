package com.thevacationplanner.app

import com.thevacationplanner.BuildConfig
import com.thevacationplanner.dto.City
import com.thevacationplanner.dto.Forecast
import com.thevacationplanner.dto.Weather
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *Created by Anderson on 08/12/2017.
 */
interface WeatherApi {
    @GET("cities/")
    fun getCities(): Observable<List<City>>

    @GET("cities/{id}/year/{year}/")
    fun getForecast(@Path("id") id: Int, @Path("year") year: Int): Observable<List<Forecast>>

    @GET("weather/")
    fun getWeatherList(): Observable<List<Weather>>

    companion object {
        fun create(): WeatherApi {

            val retrofit = Retrofit.Builder()
                    .client(OkHttpProvider.okHttpInstance)
                    .baseUrl(BuildConfig.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            return retrofit.create(WeatherApi::class.java)
        }
    }
}