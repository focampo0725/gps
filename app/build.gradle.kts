plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
//    id("com.google.gms.google-services")
//    id("io.fabric")
    id(Libs.DaggerHilt.dagger_hilt_android__plugin)
    id("maven-publish")
}

configurations {
    all {
        exclude(group = "org.json", module = "json")
    }
//    id("org.jetbrains.kotlin.android")
}

// importando el gradle de publicaciòn de la librerìa
apply(from = "./deployConfigs.gradle")

android {
    namespace = "com.kloubit.gps"
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    useLibrary("org.apache.http.legacy")
    compileSdk = 33
//    buildToolsVersion("34.0.0")

    lintOptions {
        isCheckReleaseBuilds = false
    }

    packagingOptions {
        ExcludeMetaInf.data.forEach { exclude(it) }
    }
    // Configure only for each module that uses Java 8
    // language features (either in its source code or
    // through dependencies).
    sourceSets {
        getByName("main") {
            jniLibs.srcDir("libs")
        }
    }


    defaultConfig {
        minSdk = Android.minSdkVersion
        targetSdk = Android.compileSdkVersion
        versionCode = 3
        versionName = "1.0.2"
        multiDexEnabled = true

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        applicationId = "com.kloubit.gps"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            this.abiFilters.add("arm64-v8a") // Se utiliza en la mayoría de los dispositivos Android modernos que admiten arquitecturas de 64 bits. (muy común)
//            this.abiFilters.add("x86")  //  dispositivos Android basados en procesadores Intel o procesadores x86 (dispositivos más antiguos) (poco común)
            this.abiFilters.add("armeabi-v7a")  // dispositivos más antiguos y de gama baja (muy común)
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            GradleVariants.debugConfigString.forEach {
                buildConfigField("String", it.first, it.second)
            }
            GradleVariants.productionConfigString.forEach {
                buildConfigField("String", it.first, it.second)
            }

        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            GradleVariants.debugConfigString.forEach {
                buildConfigField("String", it.first, it.second)
            }
            GradleVariants.productionConfigString.forEach {
                buildConfigField("String", it.first, it.second)
            }
//
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



//    compileSdk = 30
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.abexa.mobile-libraries:shared-mobile-library:1.35.6@aar")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.4")
    implementation("com.abexa.mobile-libraries:org.eclipse.paho.android.service.library:1.2.0@aar")
    implementation("com.abexa.mobile-libraries:mqtt-client-mobile-library:1.9.4@aar")
    implementation("com.github.Grenderg:toasty:1.5.2")


    implementation(files("libs/Native_Libs2.jar"))
    implementation(files("libs/core-3.2.1.jar"))
    implementation(files("libs/KTPsdk.aar"))
    implementation(files("libs/hmdm-1.1.7.aar"))

//    com.github.jose-jhr:Library-CameraX:1.0.8
    //machine learning
    implementation ("com.google.mlkit:text-recognition:16.0.0")

    implementation("commons-codec:commons-codec:1.15")
    implementation("androidx.appcompat:appcompat:1.4.4")
    implementation ("androidx.core:core-ktx:1.7.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.airbnb.android:lottie:6.1.0")

    Libs.AndroidUtilities.let {
        implementation(it.androidx_lifecycle__lifecycle_extensions__impl)
        // Activity Ktx for by viewModels()
        implementation(it.androidx_activity__activity_ktx__impl)
//        implementation(it.com_laimiux_lce__impl)
        implementation(it.androidx_fragment__fragment_ktx__impl)
        implementation(it.services_location)
    }

    Libs.WidgetsAndroid.let {

        implementation(it.androidx_appcompat__appcompat__impl)
        implementation(it.androidx_contraintlayout__impl)
    }

    Libs.MaterialAndroid.let {
        implementation(it.com_google_android_material__material__impl)
        implementation(it.com_github_marcoscgdev__MaterialToast__impl)
    }

    Libs.Database.let {
        implementation(it.androidx_room__room_runtime__impl)
        kapt(it.android_arch_persistence_room__compiler__kapt)
        implementation(it.android_arch_persistence_room__ktx)
        testImplementation(it.android_arch_persistence_room__testing)
    }

    Libs.DaggerHilt.let {
        implementation(it.google_dagger__hilt_android__impl)
        kapt(it.google_dagger__hilt_android_compiler__kapt)
        kapt(it.androidx_hilt__hilt_compiler__kapt)
    }
    Libs.Network.let {
        implementation(it.com_squareup_okhttp3__logging_interceptor__impl)
        implementation(it.com_squareup_okhttp3__okhttp__impl)
        implementation(it.com_squareup_retrofit2__converter_gson__impl)
        implementation(it.com_squareup_retrofit2__retrofit__impl)
        implementation(it.com_github_framontiel__persistentcookiejar__impl)
        implementation(it.com_jakewharton_retrofit__retrofit2_rxjava2_adapter__impl)
    }

    Libs.RXJava.let {
        implementation(it.com_jakewharton_retrofit__retrofit2_rxjava2_adapter__impl)
        implementation(it.io_reactivex_rxjava2__rxandroid__impl)
        implementation(it.io_reactivex_rxjava2__rxjava__impl)
    }

    Libs.PermissionsLoader.let{
        implementation(it.com_karumi__dexter__impl)
    }
}
