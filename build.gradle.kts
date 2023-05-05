import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.7.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jcenter.bintray.com/")
    }
}

dependencies {
    implementation("khttp:khttp:1.0.0")
    implementation("io.github.resilience4j:resilience4j-ratelimiter:2.0.2")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("ch.qos.logback:logback-core:1.4.7")
    implementation("ch.qos.logback:logback-classic:1.4.7")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava","compileKotlin", "processResources"))
        archiveClassifier.set("standalone")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest{
            attributes(mapOf("Main-Class" to application.mainClass))
        }
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if(it.isDirectory) it else zipTree(it) } + sourcesMain.output
        from(contents)
    }

    build {
        dependsOn(fatJar)
    }
}

application {
    mainClass.set("resilience4j.demo.DemoKt")
}