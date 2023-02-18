@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    compileSdk = 33
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "ru.kekulta.goodjobray"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true // Enables code shrinking for the release build type.
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }

    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "ru.kekulta.goodjobray"
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.legacy)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.room)
    implementation(libs.fragment.ktx)
    implementation(libs.coroutines.android)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.layout.constraint)
    implementation(project(":MinimalistViews"))
    kapt(libs.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.test)
}
