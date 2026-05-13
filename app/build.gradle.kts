import java.util.Properties
import java.io.FileInputStream
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}
val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("keystore.properties")

if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(keystorePropertiesFile.inputStream())
}

val versionPropsFile = rootProject.file("version.properties")
val versionProps = Properties()

versionProps.load(FileInputStream(versionPropsFile))

android {
    namespace = "com.qntm.cicddemotest"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    defaultConfig {
        applicationId = "com.qntm.cicddemotest"
        minSdk = 24
        targetSdk = 36
        versionCode = versionProps["VERSION_CODE"].toString().toInt()
        versionName = versionProps["VERSION_NAME"].toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
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
}
tasks.register("incrementVersionCode") {

    doLast {

        val versionPropsFile = rootProject.file("version.properties")

        val versionProps = Properties()

        versionProps.load(FileInputStream(versionPropsFile))

        var versionCode = versionProps["VERSION_CODE"].toString().toInt()

        versionCode++

        versionProps["VERSION_CODE"] = versionCode.toString()

        versionProps.store(versionPropsFile.writer(), null)

        println("Updated VERSION_CODE to $versionCode")

    }

}