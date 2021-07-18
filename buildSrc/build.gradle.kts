plugins {
  `kotlin-dsl`
  kotlin("jvm") version "1.5.10"
}

repositories {
  mavenCentral()
}
dependencies {
  implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.apply {
  jvmTarget = "1.8"
}