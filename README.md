# Anime Song Guess

Anime Song Guess is a native Android game built with Kotlin and Jetpack Compose. Create a local account, listen to clips from anime themes, and guess the anime title. Correct answers earn points and Anime Coins, which can be spent on profile customizations in the shop.

The app gets playable theme data and audio from the public AnimeThemes API. Players may also link a MyAnimeList (MAL) account to load their MAL list and enable **MAL Mode**, which chooses rounds from that list.

## What is included

- Local account registration and sign-in. Account data is stored on the device with Room; no separate backend is required.
- A music-guessing game with progressively longer audio hints, title search, scoring, and a results screen.
- A shop and profile area. Coin balances and purchased cosmetic effects are stored on the device.
- Optional MyAnimeList OAuth linking, watchlist sync, and MAL-only game rounds.

## Requirements

- Android Studio with the Android SDK Platform 37 installed. The project compiles and targets API 37.
- A physical Android device or emulator running Android 11 (API 30) or later; the app's minimum SDK is 30.
- JDK 25. The checked-in Gradle daemon configuration targets Java 25 and can download that toolchain through Foojay when needed; Android Studio can use the configured toolchain during Gradle sync.
- Internet access for Gradle's first dependency download and for gameplay. The game requests data and audio from AnimeThemes; MAL linking also needs network access.

## Clone and build

Clone the repository and enter it:

```sh
git clone https://github.com/BillyP2002/CST438-Project1-Team5.git
cd CST438-Project1-Team5
```

### Android Studio

1. Open the cloned repository root in Android Studio.
2. Allow Gradle sync to finish. Install Android SDK Platform 37 if Android Studio asks.
3. Select an API 30+ emulator or connected device.
4. Press **Run** (or choose **Run > Run 'app'**).

If Gradle cannot find the Android SDK, create a machine-local `local.properties` file at the repository root with the SDK path. Do not commit this file:

```properties
sdk.dir=/absolute/path/to/Android/Sdk
```

### Command line

With the Android SDK available to Gradle, build a debug APK using the included Gradle wrapper:

```sh
./gradlew :app:assembleDebug
```

The APK is written to:

```text
app/build/outputs/apk/debug/app-debug.apk
```

To install it from the command line on a running emulator or connected device:

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Open **Anime Song Guess** from the device launcher after installation.

## First run and configuration

No API key, environment variable, backend service, or account from the development team is needed to build or play the standard game. On first launch, choose **Create one**, register a local account (passwords must contain at least 12 characters), then sign in. The game tab loads a random public AnimeThemes round; an internet connection is required for it to load and play audio.

MAL integration is optional. To use it, select **Link MAL Account** from the sign-in screen, authorize the app in the browser, return to the app, and sign in to the local account you want to associate with that MAL account. The app's MAL OAuth client ID and callback URI are already configured in the source, so no user-provided MAL API key is required. After the list syncs, turn on **MAL Mode** in the game to select from the linked watchlist. If you skip this step, leave MAL Mode off and the rest of the app works normally.

## CI and testing

Continuous integration runs the verification suite (Detekt, Android lint, compilation, and JVM unit tests). Run the same checks locally with:

```sh
./gradlew check
```

Run JVM unit tests only:

```sh
./gradlew :app:testDebugUnitTest
```

Run Android instrumented tests on a connected API 30+ device or a running emulator:

```sh
./gradlew :app:connectedDebugAndroidTest
```

The integration tests that access a real MAL account require an OAuth token already stored on the test device; they are not required to build or run the app.
