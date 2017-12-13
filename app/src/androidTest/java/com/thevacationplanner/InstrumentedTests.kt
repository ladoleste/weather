package com.thevacationplanner

import android.os.SystemClock
import com.thevacationplanner.Constants.Companion.CLEAR
import com.thevacationplanner.Constants.Companion.COLD
import com.thevacationplanner.Constants.Companion.FAIR
import com.thevacationplanner.Constants.Companion.HOT
import com.thevacationplanner.Constants.Companion.PARTLY_CLOUDY
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

    //@Test
    fun testRioDeJaneiro() {
        onView(withId(R.id.et_number_of_days)).perform(typeText("15"))
        onView(withId(R.id.bt_cities)).perform(scrollTo(), click())
        onView(withText("Rio de Janeiro")).perform(click())
        onView(withText("Rio de Janeiro")).check(matches(isDisplayed()))
        onView(withId(R.id.bt_weather)).perform(scrollTo(), click())
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(CLEAR, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(HOT, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(PARTLY_CLOUDY, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(FAIR, click()))
        onView(withText("Clear, Hot, Partly Cloudy, Fair")).check(matches(isDisplayed()))
        onView(withText("Feito")).perform(click())
        onView(withId(R.id.bt_done)).perform(scrollTo(), click())
        SystemClock.sleep(500)//Espresso does always wait for the animation to finish
        onView(withText("De 10 de novembro a 1 de dezembro")).check(matches(isDisplayed()))
        onView(withText("OK")).perform(click())
    }

    @Test
    fun testPortoAlegre() {
        onView(withId(R.id.et_number_of_days)).perform(typeText("15"))
        onView(withId(R.id.bt_cities)).perform(scrollTo(), click())
        onView(withText("Porto Alegre")).perform(click())
        onView(withText("Porto Alegre")).check(matches(isDisplayed()))
        onView(withId(R.id.bt_weather)).perform(scrollTo(), click())
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(CLEAR, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(COLD, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(PARTLY_CLOUDY, click()))
        onView(withText("Feito")).perform(click())
        onView(withText("Clear, Cold, Partly Cloudy")).check(matches(isDisplayed()))
        onView(withId(R.id.bt_done)).perform(scrollTo(), click())
        SystemClock.sleep(500)//Espresso does always wait for the animation to finish
        onView(withText("De 20 de junho a 10 de julho")).check(matches(isDisplayed()))
        onView(withText("De 9 de maio a 26 de maio")).check(matches(isDisplayed()))
        onView(withText("De 2 de abril a 17 de abril")).check(matches(isDisplayed()))
        onView(withText("OK")).perform(click())
    }

}