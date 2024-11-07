plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.qiniu.githubstatistic"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.qiniu.githubstatistic"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.okhttp)
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation (libs.sharp.retrofit)
    implementation(libs.logging.okhttp)

    //room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    //di
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    //serialization
    implementation(libs.kotlinx.serialization.json)
    //navigation
    implementation (libs.androidx.hilt.navigation.compose)
    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    //accompanist
    implementation(libs.accompanist.permissions)
    implementation (libs.accompanist.systemuicontroller)
    implementation (libs.accompanist.swiperefresh)


}