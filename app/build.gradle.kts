import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}


android {
    namespace = "com.example.showmethemoney"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.showmethemoney"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_TOKEN",
            "\"${localProperties.getProperty("api.token")}\"")
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
}

dependencies {
    api(project(":feature:settings"))
    api(project(":domain"))
    api(project(":feature:category"))
    api(project(":feature:incomes"))
    api(project(":feature:incomes:incomeshistory"))
    api(project(":feature:expenses"))
    api(project(":feature:expenses:expenseshistory"))
    api(project(":feature:incomes:addincome"))
    api(project(":feature:expenses:addexpense"))
    api(project(":feature:account"))
    api(project(":feature:transactions"))
    implementation(project(":core:utils"))
    implementation(project(":data"))
    implementation(project(":core:ui"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.kotlinx.coroutines.core)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.androidx.work.runtime.ktx)
    implementation (libs.assisted.inject.annotations.dagger2)
    ksp (libs.assisted.inject.processor.dagger2)
}