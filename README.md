[![JitPack](https://img.shields.io/jitpack/v/github/adrielcafe/hal.svg?style=for-the-badge)](https://jitpack.io/#adrielcafe/hal) 
[![Android API](https://img.shields.io/badge/api-16%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=16) 
[![Bitrise](https://img.shields.io/bitrise/29bfee3f365ee4b9/master.svg?style=for-the-badge&token=INdPNd8bHYU2ADlFb1ZvUQ)](https://app.bitrise.io/app/29bfee3f365ee4b9) 
[![Codacy](https://img.shields.io/codacy/grade/590119aba1d14ea38908d6c1c8c11f07.svg?style=for-the-badge)](https://www.codacy.com/app/adriel_cafe/hal) 
[![kotlin](https://img.shields.io/github/languages/top/adrielcafe/hal.svg?style=for-the-badge)](https://kotlinlang.org/) 
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg?style=for-the-badge)](https://ktlint.github.io/) 
[![License MIT](https://img.shields.io/github/license/adrielcafe/hal.svg?style=for-the-badge&color=yellow)](https://opensource.org/licenses/MIT) 

# ![logo](https://github.com/adrielcafe/hal/blob/master/hal-logo.png?raw=true) HAL

🔴 **HAL** is a minimalistic [finite-state machine](https://en.wikipedia.org/wiki/Finite-state_machine) for Android &amp; JVM built with [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and [LiveData](https://developer.android.com/topic/libraries/architecture/livedata).

This project started as a library module in one of my personal projects, but I decided to open source it and add more features for general use. Hope you like!

## Usage
### TODO

## Import to your project
1. Add the JitPack repository in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

2. Next, add the desired dependencies to your module:
```gradle
dependencies {
    // Core with default state observer
    implementation "com.github.adrielcafe.hal:hal-core:$currentVersion"

    // LiveData state observer
    implementation "com.github.adrielcafe.hal:hal-livedata:$currentVersion"
}
```
Current version: [![JitPack](https://img.shields.io/jitpack/v/github/adrielcafe/hal.svg?style=flat-square)](https://jitpack.io/#adrielcafe/hal)

### Platform compatibility

|         | `hal-core` | `hal-livedata` |
|---------|--------|-----------|
| Android | ✓      | ✓         |
| JVM     | ✓      |           |
