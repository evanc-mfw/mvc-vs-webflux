rootProject.name = "mvc-webflux"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(
    "mvc",
    "shared",
    "webflux"
)