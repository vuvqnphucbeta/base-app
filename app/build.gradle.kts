import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.nio.charset.Charset
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.firebase.crashlytics)
}
abstract class GitVersionValueSource : ValueSource<String, ValueSourceParameters.None> {
    @get:Inject
    abstract val execOperations: ExecOperations

    override fun obtain(): String {
        try {
            val output = ByteArrayOutputStream()
            execOperations.exec {
                commandLine("git", "describe", "--tags", "--abbrev=0")
                standardOutput = output
            }
            return String(output.toByteArray(), Charset.defaultCharset())
        } catch (e: Exception) {
            return "v1.0.1"
        }
    }
}

val gitVersionProvider = providers.of(GitVersionValueSource::class) {}
val gitVersion = gitVersionProvider.get()

fun getVersionName(): String {
    return gitVersion.removePrefix("v").split("\n").getOrNull(0) ?: "1.0.1"
}

fun getVersionCode(): Int {
    val versionName = getVersionName()
    val parts = versionName.split(".")
    val major = parts.getOrNull(0)?.toIntOrNull() ?: 1
    val minor = parts.getOrNull(1)?.toIntOrNull() ?: 0
    val patch = parts.getOrNull(2)?.toIntOrNull() ?: 0
    return major * 10000 + minor * 100 + patch
}

// Create a variable called keystorePropertiesFile, and initialize it to your
// keystore.properties file, in the rootProject folder.
val keystorePropertiesFile = rootProject.file("local.properties")

// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "com.beta.soft"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.beta.soft"
        minSdk = 24
        targetSdk = 37
        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String", "DEFAULT_API_KEY",
            keystoreProperties.getProperty("API_KEY") ?: "\"ark_a3WOZ-jTHVo0Ku7EN8OsLYVNjt9PQ2Vn-TljxnmReD8\""
        )

        buildConfigField(
            "String",
            "LICENSE_KEY",
            "\"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvD+Kl3LXMlD7L4hnbaTlxdmeGlkrfp9J7Uz6xE8rIq1kXuR6I+uyqTd4BeyHnuLzApJcjgBNsHMOYDk+DnWxDGoAAuDbi/NxdezHT3ojVOmcRsnaP8Kyrsh9k5BPLrxtI0Sx0gaFGpHALQY7X2EDrR29RdCgd1GG3V248g9o9bXozFC35i599ULlbMrX38yBgiHhcKdjYWnUM3/mcrQrdGZTt/u7RbhwjZYWKaUERnYQJmQkHmtfWrGT966k3FmyjSXNRrtOaG+eMg1zHmHqaIwH/PFhwDe1CZv4v5NwAo5HsFPINFNicXJXf7iMUxIOMXgB5FEX4D2vyt6fMFn2ZQIDAQAB\""
        )

        buildConfigField("String", "BASE_URL", keystoreProperties.getProperty("BASE_URL") ?: "\"\"")

        // Bật/tắt gửi preference events lên server (POST /preferences/events).
        // local.properties: PREFERENCE_REPORT_ENABLED=true|false (mặc định true).
        buildConfigField(
            "Boolean",
            "PREFERENCE_REPORT_ENABLED",
            keystoreProperties.getProperty("PREFERENCE_REPORT_ENABLED")
                ?.trim()
                ?.trim('"')
                ?.equals("true", ignoreCase = true)
                ?.toString()
                ?: "true"
        )
    }
    signingConfigs {
        getByName("debug") {
            val debugKeystoreFile = file("debug.keystore")
            if (debugKeystoreFile.exists()) {
                storeFile = debugKeystoreFile
                storePassword = "android"
                keyAlias = "androiddebugkey"
                keyPassword = "android"
            }
        }

        create("release") {
            (keystoreProperties["keyAlias"] as? String)?.let {
                keyAlias = it
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }
    buildTypes {
        debug {
            /*SDK*/
            buildConfigField("String", "KEY_ADJUST", "\"keyadjust\"")
            buildConfigField("Boolean", "DEBUG_FORCE_ERROR_UI", "false")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Prefer Play-device ABIs; x86*/opencv alone added ~100MB to universal APK.
            ndk {
                abiFilters += listOf("arm64-v8a", "armeabi-v7a")
            }
            signingConfig = signingConfigs.getByName("release")
            /*SDK*/
            (keystoreProperties["keyAdjust"] as? String)?.let { keyAdjust ->
                buildConfigField("String", "KEY_ADJUST", "\"$keyAdjust\"")
            }
            (keystoreProperties["APP_ID"] as? String)?.let {
                resValue("string", "admob_app_id", it)
            }
            buildConfigField("Boolean", "DEBUG_FORCE_ERROR_UI", "false")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
            freeCompilerArgs.add("-Xskip-metadata-version-check")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
        dataBinding = true
        viewBinding = true
    }
    packaging {
        jniLibs {
            useLegacyPackaging = false
            excludes += setOf("**/libconceal.so")
        }
    }
    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {
//navigation
    implementation("androidx.navigation:navigation-compose:2.9.6")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
    // permissions
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")
    // hilt
    implementation("com.google.dagger:hilt-android:2.57.2")
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.appcompat)
    ksp("org.jetbrains.kotlin:kotlin-metadata-jvm:2.4.0")
    ksp("com.google.dagger:hilt-compiler:2.57.2")
    ksp("com.google.dagger:hilt-android-compiler:2.57.2")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    //Glide
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.16.0")
    ksp("com.github.bumptech.glide:compiler:4.16.0")
    // anim
    implementation(libs.lottie.compose)
    // firebase
    implementation(platform("com.google.firebase:firebase-bom:34.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.config)
    implementation(libs.firebase.crashlytics)
    implementation(libs.play.services.ads)
    implementation("com.google.firebase:firebase-database")

    // hawk
    implementation("com.orhanobut:hawk:2.0.1")
    implementation(libs.firebase.database)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // project
    val ktor_version = "2.3.12"
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    // Ktor Client (for peer-to-peer file transfer)
    implementation("io.ktor:ktor-client-core:${ktor_version}")
    implementation("io.ktor:ktor-client-cio:${ktor_version}")
    implementation("io.ktor:ktor-client-content-negotiation:${ktor_version}")
    // ZXing
    implementation("com.google.zxing:core:3.5.3")
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation("io.github.kyant0:backdrop:1.0.6")
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    val versionBeta = "2.2.1"
    debugImplementation("com.betasoft:android-sdk:$versionBeta-debug") {
        exclude(group = "com.google.android.libraries.ads.mobile.sdk")
    }
    releaseImplementation("com.betasoft:android-sdk:$versionBeta-release") {
        exclude(group = "com.google.android.libraries.ads.mobile.sdk")
    }
    ksp("com.betasoft:android-sdk-processor:$versionBeta")
}
