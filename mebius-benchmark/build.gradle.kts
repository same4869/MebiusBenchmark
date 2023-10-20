import com.pokemon.mebius.framework.buildsrc.AndroidX
import com.pokemon.mebius.framework.buildsrc.Dependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
//    id("com.pokemon.hikari.upload.plugin")
    id("maven-publish")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    //发布jitpack用的，私有仓库用自己的那个插件
    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("release") {
                    from(components["release"])
                    groupId = "com.pokemon.mebius"
                    artifactId = "benchmark"
                    version = "0.0.1"
                }
            }
        }
    }

}



dependencies {
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.appcompat)
    implementation(Dependencies.gson)
}