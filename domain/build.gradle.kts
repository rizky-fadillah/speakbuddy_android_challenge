plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.android.junit5)

    kotlin("kapt")
}

android {
    namespace = "jp.speakbuddy.edisonandroidexercise.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(project(":data"))
    api(project(":model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.android)

    kapt(libs.hilt.compiler)

    testImplementation(project(":testing"))
    testImplementation(libs.kotlin.test)
}