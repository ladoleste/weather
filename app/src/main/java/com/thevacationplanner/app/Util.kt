package com.thevacationplanner.app

import android.content.Context
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 *Created by Anderson on 11/12/2017.
 */
fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

fun Date.asString(format: DateFormat): String = format.format(this)

fun Date.asString(format: String = "d 'de' LLLL"): String = asString(SimpleDateFormat(format, Locale.getDefault()))

fun String.toast(context: Context) = Toast.makeText(context, this, Toast.LENGTH_LONG).show()