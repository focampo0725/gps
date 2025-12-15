object Android {
    val buildToolsVersion = "30.0.3"
    val minSdkVersion = 24
    val targetSdkVersion = 33
    val compileSdkVersion = 33
    val versionCode = 1
    val versionName = "0.1"
}


object ExcludeMetaInf {
    val data = mutableListOf(
        "META-INF/NOTICE",
        "META-INF/ASL2.0",
        "META-INF/LICENSE",
        "META-INF/versions/9/module-info.class",
        "META-INF/versions").toMutableSet()
}

object API {
    val bearerExample = "\"" + "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjEsImlhdCI6MTYxMTI0MDM5OX0.XgJ3ktJhNMzx8eVe-13bcNk4ApKOvSmfGHPDvo0yE3E" + "\""
    val jwtExample = "\"" + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjEsImlhdCI6MTYwMjEwMDg5M30.E9rUdK6iLwuF9wgCpHtwnSq3z0kEpcZ76KuMuqKx2eo"+"\""
    val hostExample = "\"" + "http://192.168.0.136:5000" + "\""
}

object BuildPlugins {
    private const val kotlinVersion = "1.7.20"
    private const val androidGradleVersion = "7.2.2"
    private const val androidMavenGradlePluginVersion = "2.1"

    val com_android_tools_build__gradle = "com.android.tools.build:gradle:$androidGradleVersion"
    val org_jetbrains_kotlin__kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    val com_github_dcendents__android_maven_gradle_plugin = "com.github.dcendents:android-maven-gradle-plugin:$androidMavenGradlePluginVersion"
}

object Libs {
    object DaggerHilt{
        private val hilt_version = "2.42"
//        private val hilt_viewmodel_version = "1.0.0-alpha03"
        private val hilCompilerVersion = "1.0.0"
        private val hiltGradlePluginVersion = "2.28-alpha"

        val google_dagger__hilt_android__impl = "com.google.dagger:hilt-android:$hilt_version"
        val google_dagger__hilt_android_compiler__kapt = "com.google.dagger:hilt-android-compiler:$hilt_version"
//        val androidx_hilt__hilt_lifecycle_viewmodel__impl = "androidx.hilt:hilt-lifecycle-viewmodel:$hilt_viewmodel_version"
        val androidx_hilt__hilt_compiler__kapt = "androidx.hilt:hilt-compiler:$hilCompilerVersion"

        val dagger_hilt_android__plugin = "dagger.hilt.android.plugin"
        val com_google_dagger__hilt_android_gradle_plugin__classpath = "com.google.dagger:hilt-android-gradle-plugin:$hiltGradlePluginVersion"
    }

    object RXJava{
        private const val retrofitRXjava2AdapterVersion = "1.0.0"
        private const val rxjavaVersion = "2.1.0"
        private const val rxandroidVersion = "2.1.0"

        // RxJava && RxAndroid
        val io_reactivex_rxjava2__rxjava__impl = "io.reactivex.rxjava2:rxjava:$rxjavaVersion"
        val io_reactivex_rxjava2__rxandroid__impl = "io.reactivex.rxjava2:rxandroid:$rxandroidVersion"
        val com_jakewharton_retrofit__retrofit2_rxjava2_adapter__impl = "com.jakewharton.retrofit:retrofit2-rxjava2-adapter:$retrofitRXjava2AdapterVersion"
    }

    object Network{
        private const val retrofitVersion = "2.3.0"
        private const val okhttpVersion = "3.9.1"
        private const val cookieJarVersion = "v1.0.1"
        private const val retrofitRXjava2AdapterVersion = "1.0.0"

        val com_squareup_okhttp3__okhttp__impl = "com.squareup.okhttp3:okhttp:$okhttpVersion"
        val com_squareup_okhttp3__logging_interceptor__impl = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
        val com_squareup_retrofit2__retrofit__impl = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        val com_squareup_retrofit2__converter_gson__impl = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
        val com_github_framontiel__persistentcookiejar__impl = "com.github.franmontiel:PersistentCookieJar:$cookieJarVersion"
        val com_jakewharton_retrofit__retrofit2_rxjava2_adapter__impl = "com.jakewharton.retrofit:retrofit2-rxjava2-adapter:$retrofitRXjava2AdapterVersion"
    }

    object ImageLoader{
        private const val picassoVersion = "2.5.2"
        private const val glideVersion = "4.11.0"

        val com_squareup_picasso__picasso__impl = "com.squareup.picasso:picasso:$picassoVersion"
        val com_github_bumptech_glide__glide__impl = "com.github.bumptech.glide:glide:$glideVersion"
        val com_github_bumptech_glide__compiler__impl = "com.github.bumptech.glide:compiler:$glideVersion"
    }

    object KotlinLibraries{
        private const val kotlinVersion = "1.3.71"

        val org_jetbrains_kotlin__kotlin_stdlib__impl = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    }

    object AndroidUtilities {
        private const val androidxLifecycle = "2.0.0"
        private const val coreKXTVersion = "1.0.0"
        private const val multidexVersion = "1.0.3"
        private const val activityKTXVersion = "1.4.0"
        private const val lceVersion = "0.3.1"
        private const val fragmentKTXVersion = "1.3.2"
        private const val locationVersion = "18.0.0"

        val androidx_lifecycle__lifecycle_extensions__impl = "androidx.lifecycle:lifecycle-extensions:$androidxLifecycle"
        val androidx_activity__activity_ktx__impl = "androidx.activity:activity-ktx:$activityKTXVersion"
        val com_laimiux_lce__impl = "com.laimiux.lce:lce:$lceVersion"
        val androidx_fragment__fragment_ktx__impl = "androidx.fragment:fragment-ktx:$fragmentKTXVersion"
        val services_location = "com.google.android.gms:play-services-location:$locationVersion"
        val androidx_core__core_ktx__impl = "androidx.core:core-ktx:$coreKXTVersion"
        val com_android_support__multidex__impl = "com.android.support:multidex:$multidexVersion"
    }

    object Database{
        private const val roomVersion = "2.4.1"
//        val android_arch_persistence_room__runtime__impl = "android.arch.persistence.room:runtime:$roomVersion"
//        val android_arch_persistence_room__compiler__kapt = "android.arch.persistence.room:compiler:$roomVersion"

//        private const val roomVersion = "2.5.0-alpha01"
        val androidx_room__room_runtime__impl = "androidx.room:room-runtime:$roomVersion"
        val android_arch_persistence_room__compiler__kapt= "androidx.room:room-compiler:$roomVersion"
        val android_arch_persistence_room__ktx= "androidx.room:room-ktx:$roomVersion"
        val android_arch_persistence_room__testing= "androidx.room:room-testing:$roomVersion"

    }
    object MaterialAndroid{
        private const val materialElementsVersion = "1.3.0"
        private const val materialToastVersion = "1.0.1"

        val com_google_android_material__material__impl = "com.google.android.material:material:$materialElementsVersion"
        val com_github_marcoscgdev__MaterialToast__impl = "com.github.marcoscgdev:MaterialToast:$materialToastVersion"
    }

    object AnimAndroid{
        private const val sequentVersion = "0.2.1"
        private const val lottieVersion = "3.4.0"

        val com_fujiyuu75__sequent__impl = "com.fujiyuu75:sequent:$sequentVersion"
        val com_airbnb_android__lottie__impl = "com.airbnb.android:lottie:$lottieVersion"
    }

    object WidgetsAndroid{
        private const val supportVersion = "27.0.2"
        private const val constraintLayoutVersion = "1.1.2"
        private const val androidxCompatVersion = "1.3.0"
        private const val androidxConstraintLayoutVersion = "2.1.3"
        private const val materialVersion = "2.1.3"

        val com_android_support__appcompat_v7__impl = "com.android.support:appcompat-v7:$supportVersion"
        val com_android_support__recyclerview_v7__impl = "com.android.support:recyclerview-v7:$supportVersion"
        val com_android_support__cardview_v7__impl = "com.android.support:cardview-v7:$supportVersion"
        val com_android_support__palette_v7__impl = "com.android.support:palette-v7:$supportVersion"
        val com_android_support__design__impl = "com.android.support:design:$supportVersion"
        val com_android_support_constraint__constraint_layout__impl = "com.android.support.constraint:constraint-layout:$constraintLayoutVersion"

        val androidx_appcompat__appcompat__impl = "androidx.appcompat:appcompat:$androidxCompatVersion"
        val androidx_contraintlayout__impl = "androidx.constraintlayout:constraintlayout:$androidxConstraintLayoutVersion"
    }

    object PermissionsLoader{
        private const val dexterVersion = "6.1.0"

        val com_karumi__dexter__impl = "com.karumi:dexter:$dexterVersion"
    }

    object Dagger2{
        private const val daggerVersion = "2.14.1"

        val com_google_dagger__dagger_compiler_kapt = "com.google.dagger:dagger-compiler:$daggerVersion"
        val com_google_dagger__dagger__impl = "com.google.dagger:dagger:$daggerVersion"
    }
}

//object TestLibs {
//    val junit = "junit:junit:4.12"
//    val mockito = "org.mockito:mockito-core:$mockitoVersion"
//    val dexmaker = "com.google.dexmaker:dexmaker:1.2"
//    val dexmaker_mockito = "com.google.dexmaker:dexmaker-mockito:1.2"
//    val annotations = "com.android.support:support-annotations:$supportVersion"
//    val espresso = "com.android.support.test.espresso:espresso-core:2.2.2"
//}