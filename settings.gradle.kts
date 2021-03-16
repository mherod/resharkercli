rootProject.name = "resharkercli"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

buildCache {
    System.getenv("GRADLE_CACHE_URL")?.let { url ->
        remote<HttpBuildCache> {
            setUrl(url)
            isPush = true
            credentials {
                username = System.getenv("GRADLE_CACHE_USERNAME")
                password = System.getenv("GRADLE_CACHE_PASSWORD")
            }
        }
    }
}
