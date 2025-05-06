import java.lang.System.getProperty
import org.jooq.meta.jaxb.Logging

plugins {
    id("common-project-conventions")
    alias(libs.plugins.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    id("nu.studer.jooq") version "9.0"
    id("org.liquibase.gradle") version "2.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jooq") {
        exclude(group = "org.jooq")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.projectreactor:reactor-test")
    runtimeOnly("com.mysql:mysql-connector-j")

    liquibaseRuntime("org.liquibase:liquibase-core:4.24.0")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
    liquibaseRuntime("com.mysql:mysql-connector-j")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    jooqGenerator("com.mysql:mysql-connector-j:8.3.0")
}

jooq {
    version = "3.19.23"
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = getProperty("jooq.url") ?: "jdbc:mysql://mysql.webflux.orb.local:3306/web_flux"
                    user = getProperty("jooq.username") ?: "root"
                    password = getProperty("jooq.password") ?: ""
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = getProperty("jooq.schema") ?: "web_flux"
                        isUnsignedTypes = false
                        excludes = "DATABASECHANGELOG|DATABASECHANGELOGLOCK"
                    }
                    target.apply {
                        packageName = "com.moneyforward.jooq.generated"
                        directory = "src/main/java"
                    }
                }
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("compileKotlin") {
    dependsOn("generateJooq")
}

tasks.named("generateJooq") {
    dependsOn("update")
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "changelogFile" to "webflux/src/main/resources/db/changelog-master.yml",
            "url" to "jdbc:mysql://mysql.webflux.orb.local:3306/web_flux",
            "username" to "root",
            "driver" to "com.mysql.cj.jdbc.Driver"
        )
    }
}


