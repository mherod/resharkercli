@file:Suppress(
    "UNUSED_VARIABLE",
    "SuspiciousCollectionReassignment"
)

import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilationToRunnableFiles
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly
import java.lang.System.getProperty

plugins {
    kotlin("multiplatform") version "1.4.30-M1"
    kotlin("plugin.serialization") version "1.4.30-M1"
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://kotlin.bintray.com/kotlinx")
}

val kotlinVersion: String = KotlinCompilerVersion.VERSION
val ktorVersion = "1.4.3"
val coroutinesVersion = "1.4.2"

group = "resharker"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm()

    val hostOs = getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    targets.flatMap(KotlinTarget::compilations).forEach { compilation ->
        compilation.kotlinOptions {
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }

    nativeTarget.apply {
        compilations {
            getByName("main").apply {
                enableEndorsedLibs = true
            }
        }
        binaries {
            executable {
                freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
                entryPoint = "resharker.cli.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3")
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion-native-mt")
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        val nativeTest by getting
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
                implementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
                implementation("junit:junit:4.13")
            }
        }
        all {
            languageSettings.apply {
                apiVersion = "1.4"
                enableLanguageFeature("InlineClasses")
                useExperimentalAnnotation("kotlin.RequiresOptIn")
                useExperimentalAnnotation("kotlinx.cli.ExperimentalCli")
            }
        }
    }
}

task<Exec>("cleanVcs") {
    workingDir(projectDir)
    inputs.files(fileTree(projectDir))
    outputs.files(fileTree(projectDir))
    commandLine("git", "clean", "-fXd", "$projectDir/")
}

task<Exec>("cleanEmptyDirs") {
    workingDir(projectDir)
    val projectSrc = "$projectDir/src/"
    inputs.files(fileTree(projectSrc))
    outputs.files(fileTree(projectSrc))
    commandLine("find", projectSrc, "-type", "d", "-empty", "-delete")
}

task<Copy>("installBinary") {
    dependsOn(tasks.getByName("build"))
    from("$buildDir/bin/native/releaseExecutable/")
    include("*.kexe")
    rename { it.substringBefore('.') }
    into("/usr/local/bin/")
}

task<JavaExec>("run") {
    dependsOn(tasks.getByName("jvmMainClasses"))
    main = "resharker.cli.MainKt"
    args("--help")
    val jvm by kotlin.targets.getting
    val main: KotlinCompilation<KotlinCommonOptions> by jvm.compilations
    val runtimeDependencies = (main as KotlinCompilationToRunnableFiles<KotlinCommonOptions>).runtimeDependencyFiles
    classpath = files(main.output.allOutputs, runtimeDependencies)
}

task<Copy>("copyReleaseBinary") {
    dependsOn(tasks.getByName("build"))
    from("$buildDir/bin/native/releaseExecutable/")
    include("*.kexe")
    rename { makeReleaseBinaryName(it) }
    into("$buildDir/artifacts")
}

fun makeReleaseBinaryName(input: String): String = buildString {
    append(input.substringBefore('.'))
    append('-')
    append(version)
    append('-')
    val host = getProperty("os.name").replace("\\s".toRegex(), "")
    val osVersion = getProperty("os.version")
    append(host.toLowerCaseAsciiOnly())
    append(osVersion.toLowerCaseAsciiOnly())
}
