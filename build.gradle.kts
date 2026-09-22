// Top-level build file where you can add configuration options common to all subprojects/modules.
// top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// Keep the conventional root Gradle command working for local development and
// CI. The Android module owns the actual checks (Detekt, lint, and JVM tests).
tasks.register("check") {
    group = "verification"
    description = "Runs all verification checks for the Android application."
    dependsOn(":app:check")
}
