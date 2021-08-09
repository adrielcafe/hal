@file:Suppress("Unused", "MayBeConstant", "MemberVisibilityCanBePrivate")

internal object Version {
    const val GRADLE_ANDROID = "7.0.0"
    const val GRADLE_DETEKT = "1.17.1"
    const val GRADLE_KTLINT = "10.1.0"
    const val GRADLE_JACOCO = "0.16.0"
    const val GRADLE_VERSIONS = "0.39.0"

    const val KOTLIN = "1.5.21"
    const val COROUTINES = "1.5.1"
    const val LIFECYCLE = "2.3.1"

    // Sample app only
    const val KTOR = "1.6.2"
    const val SERIALIZATION = "1.2.2"
    const val APP_COMPAT = "1.3.1"
    const val ACTIVITY = "1.3.1"

    const val TEST_JUNIT = "1.1.3"
    const val TEST_STRIKT = "0.31.0"
    const val TEST_MOCKK = "1.12.0"
}

object ProjectLib {
    const val ANDROID = "com.android.tools.build:gradle:${Version.GRADLE_ANDROID}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.KOTLIN}"
    const val SERIALIZATION = "org.jetbrains.kotlin:kotlin-serialization:${Version.KOTLIN}"
    const val DETEKT = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Version.GRADLE_DETEKT}"
    const val KTLINT = "org.jlleitschuh.gradle:ktlint-gradle:${Version.GRADLE_KTLINT}"
    const val JACOCO = "com.vanniktech:gradle-android-junit-jacoco-plugin:${Version.GRADLE_JACOCO}"
    const val VERSIONS = "com.github.ben-manes:gradle-versions-plugin:${Version.GRADLE_VERSIONS}"

    val all = setOf(ANDROID, KOTLIN, SERIALIZATION, DETEKT, KTLINT, JACOCO, VERSIONS)
}

object ModuleLib {
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES}"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES}"
    const val LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.LIFECYCLE}"

    // Sample app only
    const val KTOR = "io.ktor:ktor-client-android:${Version.KTOR}"
    const val KTOR_SERIALIZATION = "io.ktor:ktor-client-serialization-jvm:${Version.KTOR}"
    const val SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.SERIALIZATION}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Version.APP_COMPAT}"
    const val ACTIVITY = "androidx.activity:activity-ktx:${Version.ACTIVITY}"
    const val VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.LIFECYCLE}"

    val sample = setOf(COROUTINES_ANDROID, KTOR, KTOR_SERIALIZATION, SERIALIZATION, APP_COMPAT, ACTIVITY, VIEWMODEL)
}

object TestLib {
    const val JUNIT = "androidx.test.ext:junit-ktx:${Version.TEST_JUNIT}"
    const val STRIKT = "io.strikt:strikt-core:${Version.TEST_STRIKT}"
    const val MOCKK = "io.mockk:mockk:${Version.TEST_MOCKK}"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.COROUTINES}"

    val all = setOf(JUNIT, STRIKT, MOCKK, COROUTINES)
}
