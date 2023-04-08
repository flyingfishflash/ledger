plugins {
  id("com.diffplug.spotless")
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  typescript() {
    target("src/**/*.ts")
    prettier()
  }

  format("angular") {
    target("src/**/*.html")
    prettier()
  }

  format("css") {
    target("src/**/*.css")
    prettier()
  }

  format("scss") {
    target("src/**/*.scss")
    prettier()
  }

  json {
    target("*.json")
    prettier()
  }
}
