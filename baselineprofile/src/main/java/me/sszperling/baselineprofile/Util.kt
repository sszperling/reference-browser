package me.sszperling.baselineprofile

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By

val targetAppArg: String
    get() = InstrumentationRegistry.getArguments().getString("targetAppId")
        ?: throw Exception("targetAppId not passed as instrumentation runner arg")

object Views {
    val MacrobenchmarkScope.urlTextView get() = By.res(packageName, "mozac_browser_toolbar_origin_view")
    val MacrobenchmarkScope.urlEditView get() = By.res(packageName, "mozac_browser_toolbar_edit_url_view")
}