import com.pokemon.mebius.framework.buildsrc.AndroidX
import com.pokemon.mebius.framework.buildsrc.Dependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.pokemon.hikari.upload.plugin")
}

android {
    namespace = "com.pokemon.mebius.benchmark"
    compileSdk = com.pokemon.mebius.framework.buildsrc.Versions.compileSdkVersion

    defaultConfig {
        minSdk = com.pokemon.mebius.framework.buildsrc.Versions.minSdkVersion
        targetSdk = com.pokemon.mebius.framework.buildsrc.Versions.targetSdkVersion

        consumerProguardFiles("consumer-rules.pro")
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
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.appcompat)
    implementation(Dependencies.gson)
}