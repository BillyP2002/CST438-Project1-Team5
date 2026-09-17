import dev.detekt.gradle.Detekt

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.cst438_project1_team5"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.cst438_project1_team5"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    detektPlugins(libs.detekt.ktlint.rules)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:3.0.0")
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.foundation)
    implementation(libs.material)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation("androidx.media3:media3-exoplayer:1.11.0")
    implementation("androidx.media3:media3-ui:1.11.0")
    testImplementation(libs.junit)
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}

detekt {
    toolVersion = libs.versions.detekt.get()
    source.setFrom("src/main/java", "src/test/java", "src/androidTest/java")
    config.setFrom(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    baseline = file("config/detekt/baseline.xml")
    basePath.set(projectDir)
    // Keep advisory Detekt findings in the reports; only error-severity findings fail CI.
    failOnSeverity.set(dev.detekt.gradle.extensions.FailOnSeverity.Error)
}

tasks.withType<Detekt>().configureEach {
    reports {
        checkstyle.required.set(true)
        html.required.set(true)
        markdown.required.set(true)
        sarif.required.set(true)
    }
}
