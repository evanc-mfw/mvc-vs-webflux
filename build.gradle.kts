import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.ktlint)
}

repositories {
    mavenCentral()
}

//subprojects {
//    apply(plugin = "org.jlleitschuh.gradle.ktlint")
//    configure<KtlintExtension> {
//        outputToConsole = true
//    }
//}