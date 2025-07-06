import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

group = "io.vinicius"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bouncycastle.bcpkix)
    implementation(libs.bouncycastle.bcprov)
    implementation(libs.hibernate.utils)
    implementation(libs.jackson.jaxb)
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)
    implementation(libs.spring.data)
    implementation(libs.spring.graphql)
    implementation(libs.spring.oauth2)
    implementation(libs.spring.validation)
    implementation(libs.spring.web)

    // OpenAPI
    implementation(libs.springdoc.starter)

    annotationProcessor(libs.spring.configuration)

    developmentOnly(libs.spring.devtools)

    runtimeOnly(libs.postgres)

    testImplementation(libs.spring.graphql.test)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.webflux)
}

// Detekt
detekt {
    config.setFrom("$rootDir/config/detekt.yml")
    source.setFrom("$rootDir/src/main/kotlin")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Exec>("docker") {
    val calver = LocalDate.now().format(DateTimeFormatter.ofPattern("uu.M.d"))
    workingDir(".")
    executable("docker")
    args("build", "-t", "vegidio/tpl-spring", ".", "--build-arg", "VERSION=$calver")
}