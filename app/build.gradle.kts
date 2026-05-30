import java.util.Properties

// ─────────────────────────────────────────────────────────────────────────────
// Resolve BASE_URL: local.properties → env var → empty placeholder
// Add BASE_URL=https://api.classtrack.app/api/v1/ to local.properties or set
// the BASE_URL environment variable before building.
// ─────────────────────────────────────────────────────────────────────────────
val localProps = Properties().also { props ->
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) localFile.inputStream().use { props.load(it) }
}
val baseUrl: String =
    localProps.getProperty("BASE_URL")
        ?: System.getenv("BASE_URL")
        ?: "https://api.classtrack.app/api/v1/"

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.google.services)
}

android {
    namespace = "me.egil_accamacho.classtrack"
    compileSdk = 36

    defaultConfig {
        applicationId = "me.egil_accamacho.classtrack"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // API base URL injected at build time
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("staging") {
            initWith(getByName("debug"))
            // Hardcoded staging server — no env var needed for this build type
            buildConfigField("String", "BASE_URL", "\"http://54.209.230.174:8080/api/v1/\"")
            firebaseAppDistribution {
                appId = localProps.getProperty("FIREBASE_APP_ID")
                    ?: System.getenv("FIREBASE_APP_ID")
                    ?: ""
                releaseNotes = "Staging build v${defaultConfig.versionName} (${defaultConfig.versionCode})"
                testers = localProps.getProperty("FIREBASE_TESTERS")
                    ?: System.getenv("FIREBASE_TESTERS")
                    ?: ""
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // ── Core ────────────────────────────────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    // ── Compose BOM ─────────────────────────────────────────────────────────
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // ── Lifecycle ───────────────────────────────────────────────────────────
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    // ── Dependency Injection (Hilt) ──────────────────────────────────────────
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // ── Navigation ───────────────────────────────────────────────────────────
    implementation(libs.navigation.compose)

    // ── Networking ───────────────────────────────────────────────────────────
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)

    // ── Persistence ───────────────────────────────────────────────────────────
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.datastore.preferences)

    // ── Camera ────────────────────────────────────────────────────────────────
    implementation(libs.camerax.core)
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.view)

    // ── QR ────────────────────────────────────────────────────────────────────
    implementation(libs.mlkit.barcode.scanning)
    implementation(libs.zxing.core)

    // ── Image Loading ─────────────────────────────────────────────────────────
    implementation(libs.coil.compose)

    // ── Coroutines ────────────────────────────────────────────────────────────
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // ── Permissions ───────────────────────────────────────────────────────────
    implementation(libs.accompanist.permissions)

    // ── Google Play Services ──────────────────────────────────────────────────
    implementation(libs.play.services.location)

    // ── Testing ───────────────────────────────────────────────────────────────
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
