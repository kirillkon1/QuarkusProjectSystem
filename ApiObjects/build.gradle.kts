plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.25" // This applies the all-open plugin for JPA
    `java-library`
    `maven-publish`
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    implementation("jakarta.validation:jakarta.validation-api:3.1.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "ru.itmo"
            artifactId = "ApiObject"
            version = "1.0"
        }
    }
    repositories {
        mavenLocal() // Публикуем в локальный Maven-репозиторий
    }
}
