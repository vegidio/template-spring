import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    id(Plugins.kotlin) version Versions.kotlin
    id(Plugins.kotlin_jpa) version Versions.kotlin
    id(Plugins.kotlin_spring) version Versions.kotlin
    id(Plugins.spring) version Versions.spring
    id(Plugins.spring_boot) version Versions.spring_boot
    id(Plugins.ktlint) version Versions.ktlint
}

group = "io.vinicius"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation(Deps.bouncycastle_bcpkix)
    implementation(Deps.bouncycastle_bcprov)
    implementation(Deps.hibernate_utils)
    implementation(Deps.jackson_jaxb)
    implementation(Deps.jackson_kotlin)
    implementation(Deps.kotlin_reflect)
    implementation(Deps.kotlin_stdlib)
    implementation(Deps.spring_data)
    implementation(Deps.spring_graphql)
    implementation(Deps.spring_oauth2)
    implementation(Deps.spring_validation)
    implementation(Deps.spring_web)

    // OpenAPI
    implementation(Deps.springdoc_kotlin)
    implementation(Deps.springdoc_security)
    implementation(Deps.springdoc_ui)

    annotationProcessor(Deps.spring_configuration)

    developmentOnly(Deps.spring_devtools)

    runtimeOnly(Deps.postgres)

    testImplementation(Deps.spring_graphql_test)
    testImplementation(Deps.spring_test)
    testImplementation(Deps.spring_webflux)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
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