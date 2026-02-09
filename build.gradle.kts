import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring)
    alias(libs.plugins.spring.boot)
    // Detekt temporarily disabled - version 1.23.8 compiled with Kotlin 2.0.21, incompatible with 2.3.10
    // alias(libs.plugins.detekt)
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
    implementation(libs.jackson.jaxb)
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)
    implementation(libs.spring.actuator)
    implementation(libs.spring.data)
    implementation(libs.spring.graphql)
    implementation(libs.spring.oauth2)
    implementation(libs.spring.validation)
    implementation(libs.spring.web)

    // OpenAPI
    implementation(libs.springdoc.starter)

    annotationProcessor(libs.spring.configuration)

    developmentOnly(libs.spring.devtools)
    // Docker Compose support for automatic container management in development
    developmentOnly(libs.spring.docker.compose)

    runtimeOnly(libs.postgres)

    testImplementation(libs.spring.graphql.test)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.webflux)
}

// Force all dependencies to use the project's Kotlin version for compatibility
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(libs.versions.kotlin.get())
        }
    }
}

// KtLint configuration
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set("1.5.0") // KtLint CLI version compatible with Kotlin 2.3.10
}

// Note: Detekt unavailable - waiting for version 1.24+ with Kotlin 2.3.x support
// Use IntelliJ IDEA code inspections in the meantime

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xlambdas=indy", // Use invokedynamic for lambdas (better performance in Kotlin 2.0+)
        )
        jvmTarget.set(JvmTarget.JVM_21)
        // K2 compiler is default in Kotlin 2.0+, no need to explicitly enable it
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