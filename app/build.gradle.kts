plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.fiap.energylink"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fiap.energylink"
        minSdk = 33
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Dependências do AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit e Gson
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0")) // Firebase BOM
    implementation ("com.google.firebase:firebase-auth:22.1.1")
    implementation ("com.google.android.gms:play-services-base:18.3.0")

    implementation ("com.google.android.material:material:1.9.0")



}


// Aplica o plugin do Google Services apenas uma vez
apply(plugin = "com.google.gms.google-services")
