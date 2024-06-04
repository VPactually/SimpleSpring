plugins {
    id("java")
    application
    jacoco
}

group = "com.vpactually"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.vpactually.Application")
}

repositories {
    mavenCentral()
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.jacocoTestReport {


    reports {
        xml.required.set(true)
    }

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            exclude("com/vpactually/entities/**", "com/vpactually/dto/**")
        }
    }))

}

dependencies {
    implementation("org.springframework:spring-core:6.1.8")
    implementation("org.springframework:spring-web:6.1.8")
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.0")
    implementation("org.springframework:spring-webmvc:6.1.8")
    implementation("org.springframework:spring-context:6.1.8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.0")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    testImplementation("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.0")
    implementation("org.instancio:instancio-junit:3.3.1")
    implementation("net.datafaker:datafaker:2.0.2")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.test {
    useJUnitPlatform()
}