plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mikali.crudplayground"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mikali.crudplayground"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // ANDROID & COMPOSE DEPENDENCIES
    // Core KTX: Provides Kotlin extensions (KTX) for the Android framework, include coroutine
    implementation("androidx.core:core-ktx:1.9.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Allows you to use Compose UI toolkit's functionality within traditional Android Activities
    implementation("androidx.activity:activity-compose:1.7.2")

    // Material 3 Compose
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //An image loading library for Android backed by Kotlin Coroutines
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Navigation Compose: allowing you to navigate between composable functions
    implementation("androidx.navigation:navigation-compose:2.7.3")

    // TESTING DEPENDENCIES
    // JUnit: A testing framework to write unit tests in Java and Kotlin (run in JVM ecosystem)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")


}