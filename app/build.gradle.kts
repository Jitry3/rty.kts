
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android") version "1.9.10"
    id("com.google.gms.google-services")
}

android {
    namespace = "com.android.kvc.xiao.v2.miui"
    compileSdk = 34
    ndkVersion = "24.0.8215888"
    buildToolsVersion = "34.0.4"
    
    defaultConfig {
        applicationId = "com.android.kvc.xiao.v2.miui"
        minSdk = 24
        targetSdk = 34
        versionCode = 12
        versionName = "2023101220"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
        
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    externalNativeBuild {
        cmake {
            // 指定 CMakeLists.txt 文件的路径
            path("src/main/cpp/CMakeLists.txt")
            
        }
        
    }
    
    signingConfigs {
    
        create("app-key") {
        // 签名路径，签名文件，.bks 或 .jks
            storeFile = file("keys/arm64-v8a.keystore")
        // 签名密钥库
            storePassword = "a6uio67fg7cvuotr071gujnnx3port"
        // 签名别名
            keyAlias = "arm"
        //签名私钥
            keyPassword = "cfyut3sdgnk7ioy6fyjnd08uoteq4p"
        }
        
        create("apply") {
        // 签名路径，签名文件，.bks 或 .jks
            storeFile = file("apply/apply.keystore")
        // 签名密钥库
            storePassword = "ygsjkks3hjs8ghiks0hjijs3jjs7wry"
        // 签名别名
            keyAlias = "apply"
        //签名私钥
            keyPassword = "gyui7erjj3iokp5shjj6dhwyu9iytoi"
        }
        
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            kotlinOptions.suppressWarnings = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("app-key")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("app-key")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        
    }
    
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "17"
}

dependencies {

    val libsDir = fileTree("libs") {
        include("*.jar")
    }

    files(libsDir)

    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")
    implementation("androidx.lifecycle:lifecycle-process:2.5.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation("dev.rikka.shizuku:provider:13.1.5")
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0")
    annotationProcessor("androidx.room:room-compiler:2.5.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")
    implementation("androidx.slidingpanelayout:slidingpanelayout:1.2.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
}
