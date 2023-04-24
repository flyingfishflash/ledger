plugins { id("com.mooltiverse.oss.nyx") version "2.3.1" }

rootProject.name = "ledger"

include("backend")

include("frontend")

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories { mavenCentral() }
}
