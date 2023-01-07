import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.7"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    kotlin("plugin.jpa") version "1.8.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // just add these dependencies for use kotlin-jdsl
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    val jdslVersion = "2.1.0.RELEASE"
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter:$jdslVersion")

    // coroutine
    val coroutineVersion = "1.6.4"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutineVersion")

    // reactive
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-hibernate-reactive:$jdslVersion")
    implementation("org.hibernate.reactive:hibernate-reactive-core:1.1.9.Final")
    implementation("io.smallrye.reactive:mutiny-kotlin:2.0.0")

    // h2 db reactive
    implementation("io.agroal:agroal-pool:2.0")
    implementation("com.h2database:h2")
    implementation("io.vertx:vertx-jdbc-client:4.3.7")
}

kotlin {
    jvmToolchain(17)
}
tasks.withType<Test> {
    useJUnitPlatform()
}
