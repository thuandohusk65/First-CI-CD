@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseDistribution)
}

android {
    namespace = "dev.keego.firstci_cd"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.keego.firstci_cd"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    applicationVariants.configureEach {
        if (name.contains("debug")) {
            tasks.register("publish${name.capitalize()}") {
                dependsOn(
                    "assemble${this@configureEach.name.capitalize()}",
                    "appDistributionUpload${this@configureEach.name.capitalize()}"
                )
            }
        }
//        else {
//            variant.mergedFlavor.applicationId = "keego.volumebooster.soundbooster.soundspeaker"
//            tasks.register("publish${variant.name.capitalize()}") { dependsOn("assemble${variant.name.capitalize()}", "appDistributionUpload${variant.name.capitalize()}") }
//        }
//        tasks.named("publish${variant.name.capitalize()}") {
//            doLast {
//                def botToken = "xoxb-4958383161424-4962139707587-2H7o3UAsBRYSw6ovRkA3kMmf"
//                def channelId = "T04U6B94RCG"
// //                println("Uploading ${variant.outputs.first().outputFile} to slack")
// //                String message = "${variant.name.contains("debug") ? "🪲" : "📦"} *${variant.name.toUpperCase()}* (${defaultConfig.versionCode}) ${defaultConfig.versionName} uploaded to App Distribution!"
// //                sendApkToSlack(botToken, channelId, variant.outputs.first().outputFile, message)
// //                println(message)
//            }
//        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
//            signingConfig = signingConfigs.debug
            firebaseAppDistribution {
                artifactType = "APK"
                releaseNotesFile = "$rootDir/app/release_note.txt"
                serviceCredentialsFile = "$rootDir/app/first-ci-cd-0db6e288630d.json"
            }
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.register<Test>("runExampleUnitTest") {
    // Set the test class to execute
    filter {
        includeTestsMatching("dev.keego.firstci_cd.ExampleUnitTest")
    }

    // Configure the test environment if necessary
    testClassesDirs = files("$buildDir/tmp/kotlin-classes/debugUnitTest")
    classpath = files("$buildDir/intermediates/javac/debugUnitTest/classes")
    outputs.upToDateWhen { false } // Always run the task
}

// Make sure the custom task is run after the tests are compiled
// tasks.named("compileDebugUnitTestKotlin") {
//    finalizedBy("runExampleUnitTest")
// }

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.firebase.analytics))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}
