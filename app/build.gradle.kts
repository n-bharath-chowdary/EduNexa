plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.edunexa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.edunexa"
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
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("mysql-connector-j-8.4.0.jar"))
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
//    implementation(libs.firebase.app.check)
    implementation(libs.firebase.appcheck.safetynet)

    implementation(libs.material.v100)
    implementation(libs.meow.bottom.navigation)
    implementation(libs.kotlin.stdlib.jdk7.v1361)
    implementation(libs.meow.bottom.navigation.v131)
    implementation(libs.viewpager2)
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.jtds)
//    implementation(libs.picasso)
    implementation(libs.glide)
    implementation(libs.pdfview.android)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
}