package com.thevacationplanner.dto

import java.util.*

/**
 *Created by Anderson on 09/12/2017.
 */
data class Forecast(val date: Date, val temperature: Temperature, var weather: String, val woeid: Int)