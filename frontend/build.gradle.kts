plugins {
  id("com.diffplug.spotless")
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  // eslist is configured to format angular html templates
  typescript {
    target("src/**/*.ts", "src/**/*.html")
    targetExclude("src/polyfills.ts")
    eslint()
      .tsconfigFile("./tsconfig.json")
      .configFile("./.eslintrc.json")
  }

  format("js") {
    target("src/assets/*.js")
    prettier()
  }

  format("css") {
    target("src/**/*.css")
    prettier()
  }

  format("scss") {
    target("src/*.scss", "src/**/*.scss")
    prettier()
  }

  json {
    target("*.json", "src/*.json", "src/**/*.json")
    prettier()
  }
}
