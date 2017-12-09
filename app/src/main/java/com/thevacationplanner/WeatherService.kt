package com.thevacationplanner

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.thevacationplanner.data.City
import com.thevacationplanner.data.Forecast
import com.thevacationplanner.data.Weather
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 *Created by Anderson on 08/12/2017.
 */
interface WeatherService {
    @GET("cities/")
    fun getCities(): Observable<List<City>>

    @GET("cities/{id}/year/{year}/")
    fun getForecast(): Observable<List<Forecast>>

    @GET("weather/")
    fun getWeather(): Observable<List<Weather>>

    companion object {
        fun create(): WeatherService {

            val client = OkHttpClient.Builder()
                    .addNetworkInterceptor(StethoInterceptor())
                    .build()

            val retrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl(BuildConfig.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            return retrofit.create(WeatherService::class.java)
        }
    }
}