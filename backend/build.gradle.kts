plugins {
  java
  id("org.springframework.boot") version "3.0.2"
  id("com.diffplug.spotless")
  id("jacoco")
  id("net.ltgt.errorprone") version "3.0.1"
  //    id "com.github.spotbugs" version "5.0.6"
}

group = "net.flyingfishflash"

description = "Ledger"

val googleJavaFormatVersion: String by rootProject.extra
val scmVersion: String by rootProject.extra
val scmCommit: String by rootProject.extra
val ciPlatform: String by rootProject.extra
val ciPipelineId: String by rootProject.extra

dependencies {
  val springBootVersion = "3.0.2"
  val springSessionVersion = "3.0.0"

  implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
  implementation("com.h2database:h2:2.1.214")
  implementation("com.vladmihalcea:hibernate-types-60:2.21.1")
  implementation("commons-validator:commons-validator:1.7")
  implementation("jakarta.interceptor:jakarta.interceptor-api:2.1.0")
  implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
  implementation("jakarta.validation:jakarta.validation-api:3.0.2")
  implementation("org.flywaydb:flyway-core:9.16.1")
  implementation("org.javamoney:moneta:1.4.2")
  implementation("org.postgresql:postgresql:42.6.0")
  implementation("org.springframework.boot:spring-boot-starter-actuator:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-websocket:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
  implementation("org.springframework.session:spring-session-core:$springSessionVersion")
  implementation("org.springframework.session:spring-session-jdbc:$springSessionVersion")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
  implementation("org.zalando:jackson-datatype-money:1.3.0")
  runtimeOnly("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
  testImplementation("org.springframework.security:spring-security-test:6.0.2")
  testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
  errorprone("com.google.errorprone:error_prone_core:2.18.0")
}

jacoco { toolVersion = "0.8.8" }

sonarqube {
  properties {
    property(
        "sonar.coverage.exclusions",
        "src/main/java/net/flyingfishflash/ledger/core/CustomCommandLineRunner.java," +
            "src/main/java/net/flyingfishflash/ledger/core/configuration/**," +
            "src/main/java/net/flyingfishflash/ledger/core/multitenancy/TenantService.java," +
            "src/main/java/net/flyingfishflash/ledger/**/dto/*," +
            "src/main/java/net/flyingfishflash/ledger/*/*/*Configuration.java")
  }
}

springBoot {
  buildInfo {
    properties {
      artifact.set("ledger-backend")
      name.set("Ledger")
      version.set(scmVersion)
      additional.set(
          mapOf("ciPlatform" to ciPlatform, "ciPipelineId" to ciPipelineId, "commit" to scmCommit))
    }
  }
}

tasks {
  bootJar {}

  bootRun { jvmArgs("-Duser.timezone=UTC") }

  compileJava { options.release.set(17) }

  register<Sync>("explodeBootJar") {
    dependsOn(bootJar)
    from(project.zipTree(bootJar.get().archiveFile))
    into("$buildDir/boot_jar_exploded")
  }

  register<Copy>("copyBuildInfo") {
    mustRunAfter("explodeBootJar")
    from(layout.buildDirectory.file("boot_jar_exploded/META-INF/build-info.properties"))
    into(layout.buildDirectory.dir("boot_jar_exploded/BOOT-INF/classes/META-INF/"))
  }

  test {
    useJUnitPlatform { excludeTags("Integration") }
    testLogging { events("passed", "skipped", "failed") }
    finalizedBy("jacocoUnitTestReport")
    filter { excludeTestsMatching("net.flyingfishflash.ledger.integration*") }
  }

  register<Test>("integrationTests") {
    useJUnitPlatform {
      filter { excludeTestsMatching("net.flyingfishflash.ledger.unit*") }
      filter {
        excludeTestsMatching("net.flyingfishflash.ledger.integration.domain.accounts*nestedset*")
      }
    }
  }

  register<JacocoReport>("jacocoUnitTestReport") {
    mustRunAfter(test)
    executionData(fileTree(project.buildDir.absolutePath).include("jacoco/test.exec"))
    sourceDirectories.setFrom(files(project.sourceSets.main.get().allSource.srcDirs))
    classDirectories.setFrom(
        files(
            project.sourceSets.main.get().output.asFileTree.filter { f: File ->
              !f.path.contains("/net/flyingfishflash/ledger/core/configuration/")
            }))
    reports {
      html.required.set(true)
      xml.required.set(true)
    }
  }

  register<JacocoReport>("jacocoIntegrationTestReport") {
    mustRunAfter("integrationTests")
    executionData(fileTree(project.buildDir.absolutePath).include("jacoco/integrationTests.exec"))
    sourceDirectories.setFrom(files(project.sourceSets.main.get().allSource.srcDirs))
    classDirectories.setFrom(files(project.sourceSets.main.get().output))
    reports {
      html.required.set(true)
      xml.required.set(true)
    }
  }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  kotlinGradle { ktfmt() }

  java {
    googleJavaFormat(googleJavaFormatVersion)
    formatAnnotations()
    importOrder("java", "javax", "com", "io", "org", "", "net.flyingfishflash.ledger")
    trimTrailingWhitespace()
  }

  json {
    target("*.json")
    jackson()
  }

  format("misc") {
    target("*.md", "*.xml", ".gitignore")
    trimTrailingWhitespace()
    indentWithSpaces()
    endWithNewline()
  }
}
