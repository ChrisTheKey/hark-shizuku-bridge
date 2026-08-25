plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.christhekey.harkbridge"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.christhekey.harkbridge"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // OACP Android SDK — https://github.com/OpenAppCapabilityProtocol/oacp-android-sdk
    // Download AAR from: https://github.com/OpenAppCapabilityProtocol/oacp-android-sdk/releases
    implementation(files("libs/oacp-android-release.aar"))
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
}
