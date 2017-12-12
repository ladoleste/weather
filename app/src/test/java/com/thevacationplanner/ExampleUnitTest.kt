package com.thevacationplanner

import com.thevacationplanner.app.capitalizeWords
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun capitalizeWords() {
        assertEquals("The Vacation Planner", "the vacation planner".capitalizeWords())
    }
}
