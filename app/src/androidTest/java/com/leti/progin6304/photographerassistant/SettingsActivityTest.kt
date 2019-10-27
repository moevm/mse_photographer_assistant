package com.leti.progin6304.photographerassistant

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

    /*Проверка на отображение панели настроек*/
    @Test
    fun isVisibleSettingsScrollview() {
        Espresso.onView(ViewMatchers.withId(R.id.settings_scrollview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /*Проверка на отображение сетки 3*3*/
    @Test
    fun isSwitchGridRectangle3x3Clicked() {
        onView(withId(R.id.switchGridRectangle3x3)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isVisibleTextForSwitchGridRectangle3x3() {
        onView(withId(R.id.textForSwitchGridRectangle3x3)).check(matches(withText("Сетка 3х3")));
    }

    /*Проверка на отображение Спирали Фибоначчи*/
    @Test
    fun isSwitchGridFibClicked() {
        onView(withId(R.id.switchGridFib)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isVisibleTextForSwitchFib() {
        onView(withId(R.id.textForSwitchFib)).check(matches(withText("Спираль Фибоначчи")));
    }

    /*Проверка на отображение квадрата 1/5 от площади*/
    @Test
    fun isSwitchGridSquareClicked() {
        onView(withId(R.id.switchGridSquare)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isVisibleTextForSwitchGridSquare() {
        onView(withId(R.id.textForSwitchGridSquare)).check(matches(withText("Квадрат 1/5 площади")));
    }

    /*Проверка на отображение центра экрана*/
    @Test
    fun isSwitchGridCenterClicked() {
        onView(withId(R.id.switchGridCenter)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isVisibleTextForSwitchGridCenter() {
        onView(withId(R.id.textForSwitchGridCenter)).check(matches(withText("Центр экрана")));
    }

    /*Проверка на отображение линии горизонта*/
    @Test
    fun isSwitchHorizLineClicked() {
        onView(withId(R.id.switchHorizLine)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    @Test
    fun isVisibleTextForSwitchHorizLine() {
        onView(withId(R.id.textForSwitchHorizLine)).check(matches(withText("Линия горизонта")));
    }

    /*Проверка на отображение линии вертикали*/
    @Test
    fun isSwitchVertLineClicked() {
        onView(withId(R.id.switchVertLine)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isVisibleTextForSwitchVertLine() {
        onView(withId(R.id.textForSwitchVertLine)).check(matches(withText("Линия вертикали")));
    }

    /*Проверка на изменение цвета*/
    @Test
    fun isVisibleTextchangeColor() {
        Espresso.onView(ViewMatchers.withId(R.id.changeColor)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isClickedChangeColor() {
        onView(withId(R.id.changeColor)).check(matches(withText("Изменить цвет сетки")));
    }
    /*Проверка на отображение распознавания лица*/
    @Test
    fun isVisibleFace() {
        Espresso.onView(ViewMatchers.withId(R.id.textForSwitchFace)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isClickedFace() {
        onView(withId(R.id.textForSwitchFace)).check(matches(withText("Определение лица")));
    }
    /*Проверка на отображение изменения фильтров*/
    @Test
    fun isVisibleFilter() {
        Espresso.onView(ViewMatchers.withId(R.id.changeFilter)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun isClickedFilter() {
        onView(withId(R.id.changeFilter)).check(matches(withText("Изменить фильтр")));
    }
}