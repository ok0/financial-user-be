import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.7.5"
  id("io.spring.dependency-management") version "1.1.0"

  val kotlinVersion = "1.7.21"
  kotlin("jvm") version kotlinVersion
  kotlin("plugin.spring") version kotlinVersion
  kotlin("plugin.jpa") version kotlinVersion
  kotlin("kapt") version kotlinVersion
}

java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
  repositories {
    mavenCentral()
  }

  group = "kr.co.ok0"
  version = "0.0.1"

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = JavaVersion.VERSION_17.toString()
    }
  }
}

subprojects {
  apply {
    plugin("kotlin")
    plugin("kotlin-spring")
    plugin("kotlin-jpa")
    plugin("org.springframework.boot")
    plugin("io.spring.dependency-management")
    plugin("kotlin-allopen")
  }

  allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
  }

  tasks.bootJar {
    archiveFileName.set("app.jar")
  }
}

extra["logbackJsonVersion"] = "0.1.5"
extra["logstashLogbackEncoderVersion"] = "7.2"
allprojects {
  dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("ch.qos.logback.contrib:logback-jackson:${property("logbackJsonVersion")}")
    implementation("ch.qos.logback.contrib:logback-json-classic:${property("logbackJsonVersion")}")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:${property("logstashLogbackEncoderVersion")}")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    api("com.querydsl:querydsl-jpa")
    kapt("com.querydsl:querydsl-apt::jpa")

    runtimeOnly("mysql:mysql-connector-java")
    annotationProcessor(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")
  }
}