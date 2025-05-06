// versions are declared here since gradle libs do not work within plugin build script
val mockkVersion = "1.13.13"
val striktVersion = "0.34.0"
val kotestVersion = "5.9.1"
val kotestSpringExtensionVersion = "1.3.0"

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<Jar> {
    archiveClassifier = ""
}

dependencies {
    runtimeOnly("org.springframework.boot:spring-boot-docker-compose")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.strikt:strikt-core:$striktVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpringExtensionVersion")
}
