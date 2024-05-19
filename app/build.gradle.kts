plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Add the Google services Gradle plugin (Enlazar con firebase)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.proyecto_iurclothes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyecto_iurclothes"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    viewBinding{
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Dependencias GLIDE para cargar imagen desde una url
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //Dependencias para enlazar con Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    // Dependencia de librería de autentificación de Firebase
    implementation("com.google.firebase:firebase-auth-ktx")
    //Dependecia de Cloud Storage de Firebase
    implementation("com.google.firebase:firebase-storage")
    //Dependencia de Cloud Firestore
    implementation("com.google.firebase:firebase-firestore")
    // Dependencia de firebase analytics
    implementation("com.google.firebase:firebase-analytics")

}