package me.sszperling.baselineprofile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.Until
import me.sszperling.baselineprofile.Views.urlEditView
import me.sszperling.baselineprofile.Views.urlTextView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@RequiresApi(Build.VERSION_CODES.P)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect(
            packageName = targetAppArg,
            includeInStartupProfile = true
        ) {
            pressHome()
            startActivityAndWait()

            // this is a browser, so let's load some URL and optimize some of that too
            with(device) {
                wait(Until.findObject(urlTextView), 2000).click()
                wait(Until.findObject(urlEditView), 2000).text = "https://google.com"
                pressEnter()
            }
        }
    }
}