plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.allopen") version "2.0.21"
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
    implementation("io.quarkus:quarkus-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-rest")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    // Hibernate Reactive dependency
    implementation("io.quarkus:quarkus-hibernate-reactive")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-info")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-spring-data-jpa")
    implementation("io.quarkus:quarkus-spring-boot-properties")
    implementation("io.quarkus:quarkus-observability-devservices")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-spring-di")
    implementation("io.quarkus:quarkus-reactive-routes")

    // Reactive SQL client for PostgreSQL
    implementation("io.quarkus:quarkus-reactive-pg-client")
    implementation("io.quarkus:quarkus-hibernate-reactive-panache:3.19.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.quarkus:quarkus-arc")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    implementation("org.mindrot:jbcrypt:0.4")

    implementation("ru.itmo:ApiObject:1.0") // apiobject

    // kotlinx-serialization-json
//    implementation("io.quarkus:quarkus-rest-kotlin-serialization")
//    implementation("io.quarkus:quarkus-resteasy-reactive-kotlin-serialization:3.15.3")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    // https://mvnrepository.com/artifact/io.quarkus/quarkus-resteasy-reactive-jackson
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson:3.15.3")

    // JWT
    implementation("io.quarkus:quarkus-smallrye-jwt:3.19.2")
    implementation("io.quarkus:quarkus-smallrye-jwt-build:1.11.0.Final")

    // https://mvnrepository.com/artifact/io.vertx/vertx-web-client
    implementation("io.vertx:vertx-web-client:4.5.13")

    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")

    // https://mvnrepository.com/artifact/io.quarkus/quarkus-smallrye-reactive-messaging-kafka
    implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka:3.15.4")
}

group = "ru.itmo"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        javaParameters = true
    }
}
