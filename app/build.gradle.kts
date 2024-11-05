import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android.buildTypes.forEach { type ->
    type.buildConfigField("String", "BASE_URL",
        "\"https://gist.githubusercontent.com/hernan-uala/" +
                "dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/\"")
}

android {
    namespace = "com.javimay.uala"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.javimay.uala"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.javimay.uala.TestRunner"

        val keyStoreFile = project.rootProject.file("apikeys.properties")
        val properties = Properties()
        properties.load(keyStoreFile.inputStream())
        val mapsApiKey = properties.getProperty("MAPS_API_KEY") ?: ""

        manifestPlaceholders["MAPS_KEY"] = mapsApiKey
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes += "META-INF/LICENSE.md"
        resources.excludes += "META-INF/LICENSE-notice.md"
        resources.excludes += "META-INF/NOTICE.md"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.viewmodel)

    // Google GMS and maps
    implementation(libs.android.gms)
    implementation(libs.android.gms.maps)
    implementation(libs.google.maps.compose)

    //Hilt
    implementation (libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    implementation (libs.androidx.hilt)

    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Permissions accompanist
    implementation(libs.accompanist)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp.interceptor)

    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.executor.test)
    testImplementation(libs.kotlin.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.hilt.android)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    kaptAndroidTest(libs.dagger.hilt.compiler)
}