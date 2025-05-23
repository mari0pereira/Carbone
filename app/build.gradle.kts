plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.bike"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bike"
        minSdk = 24
        targetSdk = 35
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Retrofit para comunicação com API
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp para requisições HTTP
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // JSON e BigDecimal
    implementation("org.json:json:20210307")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")

    // Room
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.7.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Gson para conversão de JSON
    implementation("com.google.code.gson:gson:2.10")

    // Picasso para carregamento de imagens
    implementation("com.squareup.picasso:picasso:2.8")

    // Apache Commons IO para manipulação de arquivos
    implementation("commons-io:commons-io:2.11.0")

    implementation("androidx.concurrent:concurrent-futures:1.1.0")
}