plugins {
    java
    war
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.json:json:20230227")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.hibernate:hibernate-core:5.6.14.Final")
    implementation("org.hibernate:hibernate-entitymanager:5.6.14.Final")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.springframework.security:spring-security-crypto:5.7.7")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    from("C:/Users/Artem/Desktop/Учеба/2 Курс/Веб/Лаба 4/Server/web")
}
