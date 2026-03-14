plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.myperioddataismine"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.myperioddataismine"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
    }
    jvmToolchain(21)
}

dependencies {
    implementation(libs.android.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.sqlite)
    implementation(libs.google.flexbox)
    implementation(libs.sqlcipher)
}
