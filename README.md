# DroidPanel

Free, open-source, ad-free, privacy-first Android diagnostics app.
Kotlin · Jetpack Compose · Material 3 · Gradle Kotlin DSL · MVVM.

## Status: Phase 2 (live memory, battery, storage, CPU, device)

Done: Gradle/Android project with the Gradle wrapper, monochrome theme, edge-to-edge,
navigation with transitions, a dashboard with live system readings, and detail screens for
all 12 planned modules.

Live since 0.2.0 (no permissions, refreshed every 2 s while a screen is visible):

- **Memory** — total/available/used RAM, usage bar, low-memory threshold and state, app heap
  (`ActivityManager.MemoryInfo` + `Runtime`)
- **Battery** — level, charging state, power source, voltage, temperature, technology, health
  (sticky `ACTION_BATTERY_CHANGED` broadcast)
- **Storage** — data-partition total/used/free plus this app's APK and cache size (`StatFs`)
- **CPU** — core count, ABI list, hardware, per-core current/max frequency (`/sys` cpufreq)
- **Android & Device** — build fields, security patch, kernel, ART runtime, uptime

**Not done yet:** GPU, display, sensors, network, apps, camera/audio and security modules still say
"No data source yet" instead of showing invented values; the particle visualization and history
graphs are also pending.

**Build status:** verified on 2026-10-31 with JDK 17 (Temurin 17.0.21), Gradle 8.10.2,
Android Gradle Plugin 8.7.3, Kotlin 2.0.21 and Android SDK 35.
`./gradlew testDebugUnitTest assembleDebug` passes: 18/18 unit tests, `app/build/outputs/apk/debug/app-debug.apk`
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
├── data/                        device readers (memory, battery, storage, CPU, build) + formatters
├── model/DiagModule.kt          the 12 diagnostic modules and their planned scope
├── navigation/AppNavHost.kt     dashboard ↔ module detail routes
└── ui/                          theme, shared components, dashboard, detail screens
app/src/test/                    plain JUnit tests for pure logic
```

## Roadmap

2 memory/storage/battery/CPU/device data (done) · 3 particle visualization · 4 GPU/display/thermal/sensors ·
5 network/apps/camera/audio · 6 history, tests, accessibility, performance · 7 release CI.

Release signing: `docs/RELEASE_SIGNING.md`. License: MIT.
