// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        jcenter()
        mavenCentral()
        google()
        maven { setUrl("https://jitpack.io") }
    }
    dependencies {
        classpath(BuildPlugins.com_android_tools_build__gradle)
        classpath(BuildPlugins.org_jetbrains_kotlin__kotlin_gradle_plugin)
        classpath(BuildPlugins.com_github_dcendents__android_maven_gradle_plugin)
        classpath(Libs.DaggerHilt.com_google_dagger__hilt_android_gradle_plugin__classpath)
        classpath("com.android.tools.build:gradle:7.0.0")
        //        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")

    }
}

allprojects {
    repositories {
        jcenter()
        mavenCentral()
        google()
        maven { setUrl("https://jitpack.io") }
        maven { setUrl("https://repo.eclipse.org/content/repositories/paho-snapshots/") }   // MQTT snapshots

        flatDir {
            dirs("libs")
        }

        maven {
            url = uri("https://artifactory-service.abexa.pe/repository/android-libraries-releases/")
            credentials {
                username = "admin"
                password = "admin"
            }
        }
    }
}

// Si quieres, puedes descomentar esta tarea de limpieza:
//// tasks.register("clean", Delete::class) {
////     delete(rootProject.buildDir)
//// }
