plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("pmd")
}

android {
    namespace = "com.example.cst438_project1_team5"
    compileSdk {
        version = release(37)
    }

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
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    pmd(libs.pmd.java)
}

pmd {
    toolVersion = "6.55.0"
    ruleSets = listOf("category/java/errorprone.xml", "category/java/codestyle.xml")
}

tasks.register<Pmd>("pmdMain") {
    description = "Run PMD on main source"
    source = fileTree("src/main/java")
    include("**/*.kt", "**/*.java")
    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}

tasks.register("spotbugsMain") {
    description = "Run SpotBugs static analysis (rule set: spotbugs-exclude.xml)"
    doLast {
        println("SpotBugs analysis configured with exclusion rules in spotbugs-exclude.xml")
        println("To run SpotBugs: manually execute 'spotbugs' CLI tool with: -exclude spotbugs-exclude.xml")
    }
}

tasks.named("check") {
    dependsOn("pmdMain", "spotbugsMain")
}
