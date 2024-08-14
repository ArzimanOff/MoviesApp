plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.arziman_off.moviesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arziman_off.moviesapp"
        minSdk = 27
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.adapterRxjava3)
    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    implementation(libs.room)
    annotationProcessor(libs.room.annotation.processor)
    implementation(libs.room.rxjava3)
    implementation(libs.glide)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}