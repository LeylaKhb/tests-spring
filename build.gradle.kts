import java.util.*

plugins {
    id("java")
    id("org.springframework.boot") version "2.7.17"
    id("application")
    id("org.liquibase.gradle") version "2.2.0"
    id("jacoco")
}

apply(plugin = "io.spring.dependency-management")

application {
    mainClass = "itis.khabibullina.Main"
}

group = "itis.khabibullina"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.security:spring-security-taglibs:${properties["springSecurityVersion"]}")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:${properties["hibernateVersion"]}")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("org.postgresql:postgresql:42.7.2")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
    implementation("org.apache.tomcat:tomcat-jsp-api:10.1.20")
    implementation("javax.servlet.jsp:jsp-api:2.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito:mockito-junit-jupiter:5.2.0")
    implementation("com.google.code.gson:gson:${properties["gsonVersion"]}")

    testImplementation("org.projectlombok:lombok:${properties["lombokVersion"]}")
    compileOnly("org.projectlombok:lombok:${properties["lombokVersion"]}")
    annotationProcessor("org.projectlombok:lombok:${properties["lombokVersion"]}")

    implementation("org.eclipse.angus:jakarta.mail:2.0.0")
    implementation("jakarta.activation:jakarta.activation-api:2.1.3")

    implementation("org.springframework.boot:spring-boot-starter-aop")

}
configurations.all {     exclude(group = "org.eclipse.angus", module ="angus-activation") }

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}

jacoco {
    toolVersion = "0.8.11"
    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.5".toBigDecimal()
            }
        }
    }
}


var props = Properties()
props.load(file("src/main/resources/liquibase.properties").inputStream())

liquibase {
    activities.register("main") {
        arguments = mapOf(
            "changeLogFile" to props.get("change-log-file"),
            "url" to props.get("url"),
            "username" to props.get("username"),
            "password" to props.get("password"),
            "driver" to props.get("driver-class-name")
        )
    }
}

