plugins {
    id("com.android.test")
    kotlin("android")
    id("androidx.baselineprofile")
}

android {
    namespace = "me.sszperling.baselineprofile"
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        create("nightly") {}
    }

    targetProjectPath = ":app"
}

kotlin {
    jvmToolchain(Config.jvmTargetCompatibility)
}

baselineProfile {
    useConnectedDevices = true
}

dependencies {
    implementation(libs.junit.ktx)
    implementation(libs.espresso.core)
    implementation(libs.uiautomator)
    implementation(libs.benchmark)
}

androidComponents {
    beforeVariants { v ->
        if (v.name.contains("release", ignoreCase = true)) v.enable = false
    }
    onVariants { v ->
        @Suppress("UnstableApiUsage")
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { v.artifacts.getBuiltArtifactsLoader().load(it)?.applicationId }
        )
    }
}

project.configurations.configureEach {
    resolutionStrategy.capabilitiesResolution.withCapability("org.mozilla.telemetry:glean-native") {
        val toBeSelected = candidates.find {
            val id = it.id
            id is ModuleComponentIdentifier && "geckoview" in id.module
        }
        if (toBeSelected != null) {
            select(toBeSelected)
        }
        because("use GeckoView Glean instead of standalone Glean")
    }
}