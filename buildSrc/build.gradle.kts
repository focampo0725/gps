import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.9.0"
}
repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))   // compatible with jdk8, jdk11
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}