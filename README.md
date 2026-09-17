# Anime Song Guess

An Android/Jetpack Compose project for an anime-theme-song guessing game. The current app launches to a sign-in-style landing screen while the game flow is still being developed.

## Current state

`MainActivity` is the active entry point. It displays a dark-themed sign-in form with email and password fields, a **Remember me** checkbox, and placeholder actions for sign-in, password recovery, and account creation. These controls currently keep UI state only; they do not authenticate or navigate.

On launch, the app also requests one random audio record from the AnimeThemes API and logs its stream link with Android's `Audio` debug tag. The active screen does not play that audio.

The repository also contains components that are **not wired into `MainActivity` or navigation**:

- `ShopScreen`, a Compose shop prototype with eight placeholder items, a temporary 100 Anime Coin balance, cart controls, and purchase feedback.
- `soundTest.SoundTestScreen`, an ExoPlayer-based play/pause component that accepts an audio URL.
- Retrofit data-layer code for AnimeThemes audio and MyAnimeList user anime lists. No current screen invokes the MyAnimeList repository.

## Requirements

- Android Studio with Android SDK Platform 37 installed (the project compiles and targets API 37).
- An Android 11 / API 30 or newer device or emulator (the app's `minSdk` is 30).
- A Java runtime compatible with the Gradle configuration. The checked-in Gradle daemon toolchain is Java 25; Android Studio's Gradle setup can provision its configured toolchain.
- Internet access when building dependencies and when running the app's AnimeThemes request. The manifest declares the `INTERNET` permission.

## Open and run

1. Clone the repository and open its root directory in Android Studio.
2. Let Gradle sync, installing the requested SDK components if Android Studio prompts for them. If Gradle cannot locate your SDK, configure its SDK path in the local Android Studio/Gradle configuration (for example, `local.properties`); do not commit that machine-specific file.
3. Choose an API 30+ emulator or connected device, then select **Run** in Android Studio.

From a shell, use the included Gradle wrapper:

```sh
./gradlew :app:assembleDebug
```

Install the generated debug build from Android Studio, or use Android Debug Bridge with a connected device/emulator:

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Tests

### Static analysis and formatting

Detekt runs as part of `./gradlew check` and is therefore enforced by GitHub
Actions on pull requests and pushes to `main`. The configuration lives in
[`app/config/detekt/detekt.yml`](app/config/detekt/detekt.yml). It enables
Detekt's KtLint wrapper with the `android_studio` code style, which follows the
Google Android Kotlin conventions (four-space indentation, 100-character lines,
standard import ordering, and related whitespace/wrapping rules).

Run the complete local verification suite (Detekt, Android lint, compilation,
and JVM unit tests):

```sh
./gradlew check
```

Run Detekt alone:

```sh
./gradlew :app:detekt
```

To apply KtLint's safe automatic formatting locally, add `--auto-correct`:

```sh
./gradlew :app:detekt --auto-correct
```

Existing legacy findings are recorded in
[`app/config/detekt/baseline.xml`](app/config/detekt/baseline.xml), so new code
must meet the rules without requiring an unrelated cleanup first. Remove entries
from that baseline as the existing code is fixed.

Run local JVM unit tests:

```sh
./gradlew :app:testDebugUnitTest
```

Run the instrumented tests on a connected API 30+ device or running emulator:

```sh
./gradlew :app:connectedDebugAndroidTest
```

The instrumented suite includes Compose tests for the unwired shop prototype and MockWebServer-based tests for the AnimeThemes Retrofit API.
