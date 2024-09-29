plugins {
    id("com.android.application") version "8.4.2" apply false
    id("com.android.library") version "8.4.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.protobuf") version "0.9.4" apply false
    alias(libs.plugins.compose)

    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
}