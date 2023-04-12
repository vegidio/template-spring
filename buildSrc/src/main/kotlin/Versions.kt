object Versions {
    const val bouncycastle = "1.70"
    const val detekt = "1.23.0-RC1"
    const val hibernate_utils = "3.0.1"
    const val kotlin = "1.8.20"
    const val ktlint = "11.0.0"
    const val spring = "1.0.13.RELEASE"
    const val spring_boot = "2.7.3"
    const val springdoc = "1.6.14"
}

object Plugins {
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val kotlin = "org.jetbrains.kotlin.jvm"
    const val kotlin_jpa = "org.jetbrains.kotlin.plugin.jpa"
    const val kotlin_spring = "org.jetbrains.kotlin.plugin.spring"
    const val kover = "org.jetbrains.kotlinx.kover"
    const val ktlint = "org.jlleitschuh.gradle.ktlint"
    const val owasp = "org.owasp.dependencycheck"
    const val spring = "io.spring.dependency-management"
    const val spring_boot = "org.springframework.boot"
}

object Deps {
    const val bouncycastle_bcpkix = "org.bouncycastle:bcpkix-jdk15on:${Versions.bouncycastle}"
    const val bouncycastle_bcprov = "org.bouncycastle:bcprov-jdk15on:${Versions.bouncycastle}"
    const val hibernate_utils = "io.hypersistence:hypersistence-utils-hibernate-55:${Versions.hibernate_utils}"
    const val jackson_jaxb = "com.fasterxml.jackson.module:jackson-module-jaxb-annotations"
    const val jackson_kotlin = "com.fasterxml.jackson.module:jackson-module-kotlin"
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect"
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
    const val postgres = "org.postgresql:postgresql"
    const val spring_configuration = "org.springframework.boot:spring-boot-configuration-processor"
    const val spring_data = "org.springframework.boot:spring-boot-starter-data-jpa"
    const val spring_devtools = "org.springframework.boot:spring-boot-devtools"
    const val spring_graphql = "org.springframework.boot:spring-boot-starter-graphql"
    const val spring_graphql_test = "org.springframework.graphql:spring-graphql-test"
    const val spring_oauth2 = "org.springframework.boot:spring-boot-starter-oauth2-resource-server"
    const val spring_test = "org.springframework.boot:spring-boot-starter-test"
    const val spring_validation = "org.springframework.boot:spring-boot-starter-validation"
    const val spring_web = "org.springframework.boot:spring-boot-starter-web"
    const val spring_webflux = "org.springframework:spring-webflux"
    const val springdoc_kotlin = "org.springdoc:springdoc-openapi-kotlin:${Versions.springdoc}"
    const val springdoc_security = "org.springdoc:springdoc-openapi-security:${Versions.springdoc}"
    const val springdoc_ui = "org.springdoc:springdoc-openapi-ui:${Versions.springdoc}"
}