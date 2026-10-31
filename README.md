# DroidPanel

Free, open-source, ad-free, privacy-first Android diagnostics app.
Kotlin · Jetpack Compose · Material 3 · Gradle Kotlin DSL · MVVM.

## Status: Phase 1 (shell)

Done: Gradle/Android project with the Gradle wrapper, monochrome theme, edge-to-edge,
navigation with transitions, dashboard shell (real device model + Android version from
`android.os.Build`), a detail screen for each of the 12 planned modules, unit tests, CI workflow.

**Not done yet:** all measurements (RAM, battery, storage, CPU, ...), the particle visualization,
history graphs. Module screens clearly say "Not implemented yet" and show no sample data.

**Build status:** verified on 2026-10-31 with JDK 17 (Temurin 17.0.21), Gradle 8.10.2,
Android Gradle Plugin 8.7.3, Kotlin 2.0.21 and Android SDK 35.
`./gradlew testDebugUnitTest assembleDebug` passes: 7/7 unit tests, `app/build/outputs/apk/debug/app-debug.apk`
is produced and signed with the debug key.

Downloadable builds are attached to GitHub Releases (`droidpanel-<version>-debug.apk`).
Debug APKs are signed with the default debug key and are for testing only, not for store distribution.

## Build

Requirements: JDK 17, Android SDK Platform 35 + Build-Tools 35.0.0, and an `ANDROID_HOME`
(or `local.properties` with `sdk.dir=...`). The Gradle wrapper is included, so no local Gradle install is needed.

```sh
./gradlew testDebugUnitTest     # unit tests
./gradlew assembleDebug         # app/build/outputs/apk/debug/app-debug.apk
./gradlew assembleRelease       # unsigned release APK (signing: docs/RELEASE_SIGNING.md)
```

GitHub Actions (`.github/workflows/build.yml`) runs the same tests and `assembleDebug`,
then uploads `droidpanel-debug-apk` as a workflow artifact.

## Project layout

```
app/src/main/java/com/droidpanel/app/
├── MainActivity.kt              edge-to-edge Compose host
├── data/                        Build-backed device identity + repository
├── model/DiagModule.kt          the 12 diagnostic modules and their planned scope
├── navigation/AppNavHost.kt     dashboard ↔ module detail routes
└── ui/                          theme, shared components, dashboard, detail screens
app/src/test/                    plain JUnit tests for pure logic
```

## Roadmap

2 memory/storage/battery data · 3 particle visualization · 4 CPU/GPU/display/thermal/sensors ·
5 network/apps/device/camera/audio · 6 history, tests, accessibility, performance · 7 release CI.

Release signing: `docs/RELEASE_SIGNING.md`. License: MIT.
