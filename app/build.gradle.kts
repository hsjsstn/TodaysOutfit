plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    id("kotlin-kapt")
}

android {
    namespace = "ddwu.com.mobile.project.todaysoutfit"
    compileSdk = 34
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    defaultConfig {
        applicationId = "ddwu.com.mobile.project.todaysoutfit"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "MAP_KEY", "\"${getApiKey()}\"")
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
    kapt {
        correctErrorTypes = true // Annotation Processor 오류 처리 활성화
    }
}

fun getApiKey(): String {
    val apiKeyFile = file("${rootDir}/app/src/main/java/ddwu/com/mobile/project/todaysoutfit/data/ApiKey.kt")
    if (apiKeyFile.exists()) {
        return apiKeyFile.readLines().firstOrNull { it.contains("MAP_KEY") }
            ?.split("=")
            ?.get(1)
            ?.trim()
            ?.removeSurrounding("\"")
            ?: throw GradleException("MAP_KEY is not defined in ApiKey.kt")
    } else {
        throw GradleException("ApiKey.kt file not found!")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version" )

    // GoogleMap
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.libraries.places:places:3.1.0")

    // 기상청 API
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Converter ( JSON 타입 결과를 객체로 매핑 )
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Lifecycle ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
}