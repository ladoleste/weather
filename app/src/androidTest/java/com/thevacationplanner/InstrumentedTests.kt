package com.thevacationplanner

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.thevacationplanner.ui.MainActivity
import com.thevacationplanner.ui.WeatherAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTests {

    @Rule
    @JvmField
    val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testPortoAlegre() {
        onView(withId(R.id.et_number_of_days)).perform(typeText("15"))
        onView(withId(R.id.bt_cities)).perform(scrollTo(), click())
        onView(withText("Porto Alegre")).perform(click())
        onView(withId(R.id.bt_weather)).perform(scrollTo(), click())
        onView(withText("Clear")).perform(click())
        onView(withText("Cold")).perform(click())
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(26, click()))
        onView(withText("Feito")).perform(click())
        onView(withId(R.id.bt_done)).perform(scrollTo(), click())
        onView(withText("De 20 de junho a 10 de julho")).check(matches(isDisplayed()))
        onView(withText("De 9 de maio a 26 de maio")).check(matches(isDisplayed()))
        onView(withText("De 2 de abril a 17 de abril")).check(matches(isDisplayed()))
        onView(withText("OK")).perform(click())
    }
}