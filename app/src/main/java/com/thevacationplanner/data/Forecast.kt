package com.thevacationplanner.data

import java.util.*

/**
 *Created by Anderson on 09/12/2017.
 */
data class Forecast(val date: Date, val temperature: Temperature, val weather: String, val woeid: Int)