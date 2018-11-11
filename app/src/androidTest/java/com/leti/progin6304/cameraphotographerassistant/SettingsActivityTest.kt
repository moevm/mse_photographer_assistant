package com.leti.progin6304.cameraphotographerassistant

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class SettingsActivityTest {
    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule(SettingsActivity::class.java)

    @Test
    fun isVisibleSettingsScrollview() {
        Espresso.onView(ViewMatchers.withId(R.id.settings_scrollview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isVisibleTextDeveloped() {
        onView(withId(R.id.test_textview)).check(matches(withText("Developed by @kovinevmv, Jools")));
    }

    @Test
    fun isVisibleTextForSwitchFib() {
        onView(withId(R.id.textForSwitchFib)).check(matches(withText("Спираль Фибоначчи")));
    }

    @Test
    fun isVisibleTextForSwitchGridSquare() {
        onView(withId(R.id.textForSwitchGridSquare)).check(matches(withText("Квадрат 1/5 площади")));
    }

    @Test
    fun isVisibleTextForSwitchGridCenter() {
        onView(withId(R.id.textForSwitchGridCenter)).check(matches(withText("Центр экрана")));
    }

    @Test
    fun isVisibleTextForSwitchHorizLine() {
        onView(withId(R.id.textForSwitchHorizLine)).check(matches(withText("Линия горизонта")));
    }

    @Test
    fun isVisibleTextForSwitchVertLine() {
        onView(withId(R.id.textForSwitchVertLine)).check(matches(withText("Линия вертикали")));
    }

    @Test
    fun isVisibleTextchangeColor() {
        onView(withId(R.id.changeColor)).check(matches(withText("Изменить цвет сетки")));
    }

    @Test
    fun isSwitchGridRectangle3x3Clicked() {
        onView(withId(R.id.switchGridRectangle3x3)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSwitchGridFibClicked() {
        onView(withId(R.id.switchGridFib)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSwitchGridSquareClicked() {
        onView(withId(R.id.switchGridSquare)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSwitchGridCenterClicked() {
        onView(withId(R.id.switchGridCenter)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSwitchHorizLineClicked() {
        onView(withId(R.id.switchHorizLine)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isSwitchVertLineClicked() {
        onView(withId(R.id.switchVertLine)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}