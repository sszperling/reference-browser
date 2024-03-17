package me.sszperling.baselineprofile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
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
@RequiresApi(Build.VERSION_CODES.N)
class LoadUrlBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
        benchmark(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfiles() =
        benchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun benchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = targetAppArg,
            metrics = listOf(FrameTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = {
                pressHome()
            }
        ) {
            device.executeShellCommand("pm clear $packageName")
            startActivityAndWait()
            with(device) {
                wait(Until.findObject(urlTextView), 2000).click()
                wait(Until.findObject(urlEditView), 2000).text = "https://google.com"
                pressEnter()
                waitForIdle(5000)
            }
        }
    }
}