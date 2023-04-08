import java.io.ByteArrayOutputStream

plugins {
  id("com.diffplug.spotless") version "6.18.0"
  id("com.github.ben-manes.versions") version "0.46.0"
  id("org.sonarqube") version "4.0.0.2929"
}

description =
    "A Java based bookkeeping engine and API server, along with an Angular based web application for personal finance management."

fun String.runCommand(currentWorkingDir: File = file("./")): String {
  val byteOut = ByteArrayOutputStream()
  project.exec {
    workingDir = currentWorkingDir
    commandLine = this@runCommand.split("\\s".toRegex())
    standardOutput = byteOut
  }
  return String(byteOut.toByteArray()).trim()
}

val googleJavaFormatVersion by extra { "1.15.0" }
val scmVersion by extra { "./scripts/version.sh version".runCommand() }
val scmCommit by extra { "./scripts/version.sh commit".runCommand() }
val ciPlatform by extra { "./scripts/version.sh ci-platform".runCommand() }
val ciPipelineId by extra { "./scripts/version.sh ci-pipeline-id".runCommand() }

tasks.register("printVersionScriptOutput") {
  doLast { project.exec { commandLine("./scripts/version.sh", "all") } }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  kotlinGradle { ktfmt() }

  json {
    target("*.json")
    prettier()
  }

  yaml {
    target("scripts/release/*.yml")
    jackson().feature("ORDER_MAP_ENTRIES_BY_KEYS", true)
  }

  format("misc") {
    target("*.md", "*.xml", ".gitignore")
    trimTrailingWhitespace()
    indentWithSpaces()
    endWithNewline()
  }
}
