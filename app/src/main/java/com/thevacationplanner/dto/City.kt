package com.thevacationplanner.dto

/**
 *Created by Anderson on 08/12/2017.
 */
data class City(
        val woeid: Int = 0,
        val district: String = "",
        val province: String = "",
        val state_acronym: String = "",
        val country: String = ""
)