package com.thevacationplanner.app

import android.util.Log
import timber.log.Timber

/**
 *Created by Anderson on 08/12/2017.
 */
class SuperLog : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return super.createStackElementTag(element) + "|" + element.lineNumber
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        //todo automatic conversion from Java, needs to be rewritten
        Log.println(priority, "MyApp", String.format(".(%s.java:%s) - %s", tag!!.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0], tag.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1], message))
    }
}