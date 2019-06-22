@file:Suppress("Unused", "MayBeConstant", "MemberVisibilityCanBePrivate")

internal object Version {
    const val GRADLE_ANDROID = "3.4.1"
    const val GRADLE_DETEKT = "1.0.0-RC15"
    const val GRADLE_KTLINT = "8.1.0"
    const val GRADLE_VERSIONS = "0.21.0"
    const val GRADLE_MAVEN = "2.1"

    const val KOTLIN = "1.3.40"
    const val COROUTINES = "1.2.1"
    const val LIVEDATA = "2.0.0"

    // Sample only
    const val APP_COMPAT = "1.1.0-beta01"
    const val LIFECYCLE = "2.2.0-alpha01"
    const val VIEWMODEL_KTX = "2.2.0-alpha01"
    const val ACTIVITY_KTX = "1.0.0-beta01"
}

object ProjectLib {
    const val ANDROID = "com.android.tools.build:gradle:${Version.GRADLE_ANDROID}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.KOTLIN}"
    const val DETEKT = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Version.GRADLE_DETEKT}"
    const val KTLINT = "org.jlleitschuh.gradle:ktlint-gradle:${Version.GRADLE_KTLINT}"
    const val VERSIONS = "com.github.ben-manes:gradle-versions-plugin:${Version.GRADLE_VERSIONS}"
    const val MAVEN = "com.github.dcendents:android-maven-gradle-plugin:${Version.GRADLE_MAVEN}"

    val all = setOf(ANDROID, KOTLIN, DETEKT, KTLINT, VERSIONS, MAVEN)
}

object ModuleLib {
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.KOTLIN}"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"
    const val LIVEDATA = "androidx.lifecycle:lifecycle-livedata:${Version.LIVEDATA}"

    const val APP_COMPAT = "androidx.appcompat:appcompat:${Version.APP_COMPAT}"
    const val LIFECYCLE = "androidx.lifecycle:lifecycle-extensions:${Version.LIFECYCLE}"
    const val VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.VIEWMODEL_KTX}"
    const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${Version.ACTIVITY_KTX}"
}