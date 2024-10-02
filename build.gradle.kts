plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    alias(libs.plugins.hilt) apply false
    id("com.google.protobuf") version "0.9.4" apply false
    id("de.mannodermaus.android-junit5") version "1.11.0.0" apply false
    alias(libs.plugins.compose)

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}