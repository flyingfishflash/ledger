plugins {
  id("com.diffplug.spotless") version "6.21.0"
  id("com.github.ben-manes.versions") version "0.48.0"
  id("org.sonarqube") version "4.3.1.3277"
}

description =
    "A Java based bookkeeping engine and API server, along with an Angular based web application for personal finance management."

val googleJavaFormatVersion by extra { "1.17.0" }
val ciCommit by extra { ciCommit() }
val ciPlatform by extra { ciPlatform() }
val ciPipelineId by extra { ciPipelineId() }

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  kotlinGradle { ktfmt() }

  json {
    target("*.json")
    prettier()
  }

  yaml {
    target("scripts/ci/drone/deploy/*.yaml")
    prettier()
    //    jackson().feature("ORDER_MAP_ENTRIES_BY_KEYS", true)
  }

  format("misc") {
    target("*.md", "*.xml", ".gitignore")
    trimTrailingWhitespace()
    indentWithSpaces()
    endWithNewline()
  }
}

tasks {
  register("writeVersionToFile") {
    doLast { File(".version").writeText(project.version.toString()) }
  }

  register("writeVersionToTagsFile") {
    doLast { File(".tags").writeText(project.version.toString()) }
  }
}

fun ciPlatform(): String {
  var ciPlatform = "Non-CI Build"
  if (System.getenv("CI") == "true") {
    if (System.getenv("DRONE") == "true") {
      ciPlatform = "drone"
    } else if (System.getenv("GITLAB_CI") == "true") {
      ciPlatform = "gitlab"
    }
  }
  return ciPlatform
}

fun ciPipelineId(): String {
  var ciPipelineId = "0"
  if (ciPlatform() == "drone") {
    ciPipelineId = System.getenv("DRONE_BUILD_NUMBER")
  } else if (ciPlatform() == "gitlab") {
    ciPipelineId = System.getenv("CI_PIPELINE_ID")
  }
  return ciPipelineId
}

fun ciCommit(): String {
  var ciCommit = "No Commit SHA"
  if (ciPlatform() == "drone") {
    ciCommit = System.getenv("DRONE_COMMIT_SHA").slice(0..7)
  } else if (ciPlatform() == "gitlab") {
    ciCommit = System.getenv("CI_COMMIT_SHORT_SHA")
  }
  return ciCommit
}
