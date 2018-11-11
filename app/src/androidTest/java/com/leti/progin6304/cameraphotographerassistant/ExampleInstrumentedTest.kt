package com.leti.progin6304.cameraphotographerassistant

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule(MainActivity::class.java)

    /*Проверка на отображение главной панели*/
    @Test
    fun isVisibleCamera() {
        onView(withId(R.id.camera_preview)).check(matches(isDisplayed()))
    }

    /*Проверка на отображение снимка*/
    @Test
    fun isClickableButton() {
        onView(withId(R.id.shutter)).perform(click())
                .check(matches(isDisplayed()))
    }

    /*Проверка на отображение смены камеры*/
    @Test
    fun isClickableButtonSwitchCamera() {
        onView(withId(R.id.switchCamera)).perform(click())
                .check(matches(isDisplayed()))
    }

    /*Проверка на отображение панели настроек*/
    @Test
    fun isClickableButtonSettings() {
        onView(withId(R.id.settings)).perform(click())
    }
}
