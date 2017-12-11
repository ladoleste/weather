package com.thevacationplanner.global

/**
 *Created by Anderson on 11/12/2017.
 */
fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { it.capitalize() }
}