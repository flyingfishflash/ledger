plugins { id("com.mooltiverse.oss.nyx") version "2.3.1-hotfix186.1" }

rootProject.name = "ledger"

include("backend")

include("frontend")

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories { mavenCentral() }
}

configure<com.mooltiverse.oss.nyx.gradle.NyxExtension> {
  dryRun.set(true)
  preset.set("simple")
  verbosity.set("DEBUG")

  commitMessageConventions {
    enabled.set(listOf("conventionalCommitsCustom"))
    items {
      create("conventionalCommitsCustom") {
        expression.set(
            "(?m)^(?<type>[a-zA-Z0-9_]+)(!)?(\\\\((?<scope>[a-z ]+)\\\\))?:( (?<title>.+))\$(?s).*")
        bumpExpressions.set(
            mapOf(
                "major" to "(?s)(?m)^[a-zA-Z0-9_]+(!|.*^(BREAKING( |-)CHANGE: )).*",
                "minor" to "(?s)(?m)^feat(?!!|.*^(BREAKING( |-)CHANGE: )).*",
                "patch" to "(?s)(?m)^fix|^refactor(?!!|.*^(BREAKING( |-)CHANGE: )).*"))
      }
    }
  }

  releaseTypes {
    enabled.set(listOf("mainline", "internal"))
    items {
      create("mainline") {
        filterTags.set("^(listOf(0-9)\\d*)\\.(listOf(0-9)\\d*)\\.(listOf(0-9)\\d*)$")
        gitCommit.set("false")
        gitPush.set("true")
        gitTag.set("true")
        matchBranches.set("^(master|main)\$")
        matchEnvironmentVariables.set(mapOf("CI" to "^true$"))
        publish.set("true")
      }

      create("internal") {
        collapseVersions.set(true)
        collapsedVersionQualifier.set("{{#lower}}{{branch}}{{/lower}}")
        filterTags.set("^(listOf(0-9)\\d*)\\.(listOf(0-9)\\d*)\\.(listOf(0-9)\\d*)$")
        gitCommit.set("false")
        gitPush.set("false")
        gitTag.set("false")
        publish.set("false")
      }
    }
  }
}
