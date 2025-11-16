plugins {
    java
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("checkstyle")
    id("com.diffplug.spotless") version "6.25.0"
}

group = "com.athenhub"
version = "0.0.1-SNAPSHOT"
description = "project-interface"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven {
        name = "GitHubPublicPackages"
        url = uri("https://maven.pkg.github.com/athenhub/common")
        credentials {
            username = (project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")) as String?
            password = (project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")) as String?
        }
    }
}

val mockitoAgent: Configuration = configurations.create("mockitoAgent")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // common
    implementation("com.athenhub:common:0.2.1")

    // gson
    implementation("com.google.code.gson:gson")

    // lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")

    // mockito - 모킹 시 사용
    testImplementation("org.mockito:mockito-core")

    // archunit - 아키텍처 검증 테스트에 사용
    testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")

    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // mockito 관련 경고 해결을 위해 java agent를 명시적으로 추가
    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}


checkstyle {
    toolVersion = "12.1.2"
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    isShowViolations = true
}

tasks.withType<Checkstyle> {
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}

tasks.named("check") {
    dependsOn("checkstyleMain", "checkstyleTest")
}

spotless {
    java {
        googleJavaFormat() // Google Java 스타일 강제
        target("src/**/*.java")
    }
}

tasks.check {
    dependsOn("spotlessCheck")
}
