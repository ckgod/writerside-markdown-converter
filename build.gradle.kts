import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java") // 자바 지원을 위한 플러그인
    alias(libs.plugins.kotlin) // Kotlin 지원을 위한 플러그인
    alias(libs.plugins.intelliJPlatform) // IntelliJ 플랫폼 Gradle 플러그인
    alias(libs.plugins.changelog) // JetBrains에서 제공하는 Gradle Changelog Plugin을 사용해 CHANGELOG.md 파일 관리, 패치, 릴리스 노트 생성 등에 활용
    alias(libs.plugins.qodana) // Qodana - JetBrains에서 만든 AI 코드 리뷰어, 코드 정적 분석 후 버그, 성능 문제, 안 좋은 코드 습관 등 찾아내고 리포트 생성
    alias(libs.plugins.kover) // Gradle Kover Plugin
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

// kotlin 컴파일러가 JDK 21을 사용하도록 설정
kotlin {
    jvmToolchain(21)
}

repositories {
    // 라이브러리, 플러그인 다운로드할 기본 저장소 지정
    mavenCentral()

    // Intellij Platform 관련 기본 저장소 지정
    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-repositories-extension.html
    intellijPlatform {
        defaultRepositories()
    }
}

// Dependencies are managed with Gradle version catalog - read more: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.opentest4j)
    implementation(libs.google.genai)

    // coroutines: SpillinKt ClassNotFound 에러 해결 - version catalog 에 선언하면 gradle sync 에러남 (원인 불명)
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.2.0")

    // intellij Platform 관련 설정
    intellijPlatform {
        intellijIdeaCommunity("2024.3.6")

        // https://plugins.jetbrains.com/docs/intellij/plugin-dependencies.html#intellij-platform-gradle-plugin-2x
        bundledPlugins("org.jetbrains.kotlin")
        // bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })
        // plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })

        testFramework(TestFrameworkType.Platform)  // 플랫폼 테스트 프레임워크 설정
    }
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    // 플러그인(Plugin.xml 등)에 들어갈 여러 정보(버전, 설명, 체인지 노트 등)를 설정하는 블록
    pluginConfiguration {
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")

        // README.md에서 특정 구간 파싱 - start, end 구간을 찾아 그 사이 내용을 HTML로 변환하여 플러그인 설명으로 설정
        // 실제 plugin.xml 또는 Marketplatce 업로드 시 표시되는 설명이 됨
        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        // CHANGELOG.md에서 현재 버전에 대한 릴리스 노트("changeNotes")를 추출해 자동으로 플러그인 메타 정보에 넣는다.
        // renderItem과 withHeader(false), withEmptySections(false) 등을 통해 Markdown을 HTML 형태로 변환하여 릴리즈 노트로 활용한다.
        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes = providers.gradleProperty("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
                    Changelog.OutputType.HTML,
                )
            }
        }

        // 플러그인이 호환되는 IDE 빌드 범위를 설정한다.
        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
        }
    }

    // 플러그인에 서명할 때 필요한 인증서 정보를 환경변수에서 로드
    // MarketPlace에 서명된 플러그인을 제출하려면 설정이 필요함
    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    // Marketplace나 다른 채널로 플러그인을 업로드할 때 필요한 설정
    publishing {
        // MarketPlace에 인증된 토큰 값
        token = providers.environmentVariable("PUBLISH_TOKEN")

        // channels 설정을 통해 버전이 2.1.7-alpha.3 같은 식이면 alpha 채널로 배포하는 방식의 로직을 처리
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels = providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
    }

    // JetBrains가 권장하는 다양한 IDE 버전에 대해 플러그인을 자동 검증하도록 세팅한다.
    pluginVerification {
        ides {
            recommended()
        }
    }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty()
    repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}

tasks {
    wrapper {
        gradleVersion = providers.gradleProperty("gradleVersion").get()
    }

    publishPlugin {
        dependsOn(patchChangelog)
    }
}

intellijPlatformTesting {
    runIde {
        register("runIdeForUiTests") {
            task {
                jvmArgumentProviders += CommandLineArgumentProvider {
                    listOf(
                        "-Drobot-server.port=8082",
                        "-Dide.mac.message.dialogs.as.sheets=false",
                        "-Djb.privacy.policy.text=<!--999.999-->",
                        "-Djb.consents.confirmation.enabled=false",
                    )
                }
            }

            plugins {
                robotServerPlugin()
            }
        }
    }
}
