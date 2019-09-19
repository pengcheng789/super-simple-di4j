plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.50"
    java
}

group = "cn.caipengcheng"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.slf4j:slf4j-api:1.7.28")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    testImplementation("junit:junit:4.12")
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "cn.caipengcheng.ssimpledi4j.SSimpleDI4jKt"
    }

    from(configurations.runtimeClasspath.get().files.map { if (it.isDirectory) it else zipTree(it) })
}