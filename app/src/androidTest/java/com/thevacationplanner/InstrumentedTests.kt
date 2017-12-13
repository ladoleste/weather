package com.thevacationplanner

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.thevacationplanner.Constants.Companion.CLEAR
import com.thevacationplanner.Constants.Companion.COLD
import com.thevacationplanner.Constants.Companion.FAIR
import com.thevacationplanner.Constants.Companion.HOT
import com.thevacationplanner.Constants.Companion.PARTLY_CLOUDY
import com.thevacationplanner.app.OkHttpProvider
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
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testRioDeJaneiro() {

        val idlingResource = OkHttp3IdlingResource.create("okhttp", OkHttpProvider.okHttpInstance)
        IdlingRegistry.getInstance().register(idlingResource)

        onView(withId(R.id.et_number_of_days)).perform(typeText("15"))
        onView(withId(R.id.bt_cities)).perform(scrollTo(), click())
        onView(withText("Rio de Janeiro")).perform(click())
        onView(withId(R.id.tv_selected_city)).check(matches(withText("Rio de Janeiro")))
        onView(withId(R.id.bt_weather)).perform(scrollTo(), click())
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(CLEAR, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(HOT, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(PARTLY_CLOUDY, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(FAIR, click()))
        onView(withText("Feito")).perform(click())
        onView(withText("Clear, Hot, Partly Cloudy, Fair")).check(matches(isDisplayed()))
        onView(withId(R.id.bt_done)).perform(scrollTo(), click())
        onView(withText("De 10 de novembro a 1 de dezembro")).check(matches(isDisplayed()))
        onView(withText("OK")).perform(click())

        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun testPortoAlegre() {

        val idlingResource = OkHttp3IdlingResource.create("okhttp", OkHttpProvider.okHttpInstance)
        IdlingRegistry.getInstance().register(idlingResource)

        onView(withId(R.id.et_number_of_days)).perform(typeText("15"))
        onView(withId(R.id.bt_cities)).perform(scrollTo(), click())
        onView(withText("Porto Alegre")).perform(click())
        onView(withId(R.id.tv_selected_city)).check(matches(withText("Porto Alegre")))
        onView(withText("Porto Alegre")).check(matches(isDisplayed()))
        onView(withId(R.id.bt_weather)).perform(scrollTo(), click())
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(CLEAR, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(COLD, click()))
        onView(withId(R.id.rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition<WeatherAdapter.ViewHolder>(PARTLY_CLOUDY, click()))
        onView(withText("Feito")).perform(click())
        onView(withText("Clear, Cold, Partly Cloudy")).check(matches(isDisplayed()))
        onView(withId(R.id.bt_done)).perform(scrollTo(), click())
        onView(withText("De 20 de junho a 10 de julho")).check(matches(isDisplayed()))
        onView(withText("De 9 de maio a 26 de maio")).check(matches(isDisplayed()))
        onView(withText("De 2 de abril a 17 de abril")).check(matches(isDisplayed()))
        onView(withText("OK")).perform(click())

        IdlingRegistry.getInstance().unregister(idlingResource)
    }

}