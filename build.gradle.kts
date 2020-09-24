import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.0"
    id("com.adarshr.test-logger") version "2.1.0"

    `java-library`
}

repositories {
    jcenter()
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.9-native-mt-2")

    implementation("com.atlassian.jira:jira-rest-java-client-core:5.2.1")

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation(kotlin("stdlib"))

    val koTestVersion = "4.2.5"

    testImplementation("io.kotest:kotest-runner-junit5:$koTestVersion") // for kotest framework
    testImplementation("io.kotest:kotest-assertions-core:$koTestVersion") // for kotest core jvm assertions
    testImplementation("io.kotest:kotest-property:$koTestVersion") // for kotest property test
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.4"
        apiVersion = "1.4"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
