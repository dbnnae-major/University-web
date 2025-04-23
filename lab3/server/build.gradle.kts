plugins {
    id("java")
    id("war")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.hibernate:hibernate-core:5.6.14.Final")
    implementation("org.hibernate:hibernate-entitymanager:5.6.14.Final")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("javax.inject:javax.inject:1")
    implementation("javax.persistence:javax.persistence-api:2.2")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("javax.faces:javax.faces-api:2.3")

    implementation("org.glassfish:javax.faces:2.3.9")
    implementation("jstl:jstl:1.2")
    implementation("org.json:json:20230227")



    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
