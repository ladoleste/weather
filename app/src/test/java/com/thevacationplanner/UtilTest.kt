package com.thevacationplanner

import com.thevacationplanner.app.asString
import com.thevacationplanner.app.capitalizeWords
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilTest {
    @Test
    fun capitalizeWords() {
        assertEquals("The Vacation Planner", "the vacation planner".capitalizeWords())
    }

    @Test
    fun dateAsString() {
        assertEquals("Wed, Dec 31", Date(3443343).asString("EEE, MMM d"))
    }
}
