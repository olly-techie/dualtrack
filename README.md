# DualTrack

An offline-first Android app for planning life as a tech founder **and** a university student.
One calendar, two modes — **Founder** (amber) and **Student** (blue) — so a pitch meeting
and an exam can live in the same timeline without getting mixed up.

Built with Kotlin + Jetpack Compose + Room. No backend, no accounts, no network required.

## What's in v0.1

- **Unified timeline** of tasks grouped by day (Today / Yesterday / Tomorrow / date)
- **Mode switcher**: All / Founder / Student filter
- **Add task** dialog with title, note, mode, and date picker
- **Complete** a task by tapping its checkbox; **delete** by long-pressing the card
- Distinct color coding per mode; Material 3 with dynamic color + dark theme
- Local Room database (survives app restarts)

## Building without Android Studio (cloud build)

You don't need Android Studio or a local Android SDK. This repo includes a GitHub Actions
workflow that builds the debug APK in the cloud:

1. Create a GitHub account (if you don't have one) at https://github.com/signup.
2. Upload this folder as a new repository:
   - Go to https://github.com/new, name it e.g. `dualtrack`.
   - On your machine, from this folder:
     ```bash
     git init
     git add .
     git commit -m "Initial DualTrack scaffold"
     git branch -M main
     git remote add origin https://github.com/YOUR_USERNAME/dualtrack.git
     git push -u origin main
     ```
3. The workflow in `.github/workflows/build-apk.yml` runs automatically on every push to
   `main`. You can also trigger it manually: **Actions** tab → **Build DualTrack APK** →
   **Run workflow**.
4. When the build finishes, open the run → scroll to **Artifacts** → download
   `dualtrack-debug-apk`. It's a ZIP; extract it to get `app-debug.apk`.
5. On your Android phone:
   - Transfer the APK (USB, Google Drive, etc.) and open it.
   - Allow "Install unknown apps" for the app you opened it from.
   - Install. (You'll get a "built with a debug certificate" warning — that's normal.)

This gives you a real, installable APK with no local SDK and no IDE.

## Requirements (if you ever want a local build)

- **Android Studio** (Ladybug or newer) — it bundles the JDK, Gradle, and Android SDK.
- An Android device or emulator running **API 26+** (Android 8.0 Oreo).

You do **not** need a local Java SDK to open this project — Android Studio provides one.
If you prefer the CLI and have Java + the Android SDK installed:

```bash
./gradlew assembleDebug      # build a debug APK -> app/build/outputs/apk/debug/
./gradlew installDebug       # install on a connected device/emulator
./gradlew test               # unit tests
```

The Gradle wrapper JAR is bundled, so `./gradlew` works out of the box. Opening the project
in Android Studio also regenerates it automatically if needed.

## Getting started

1. Open Android Studio → **Open** → select this folder (`momenta`).
2. Let Gradle sync finish (first sync downloads dependencies).
3. Run the `app` configuration on an emulator or connected device.

## Project layout

```
app/src/main/java/com/dualtrack/app/
├── MainActivity.kt                  # Compose entry point
├── data/
│   ├── Mode.kt                      # Founder / Student enum
│   ├── Task.kt                      # Room entity
│   ├── Converters.kt                # enum <-> String
│   ├── TaskDao.kt                   # Flow-based queries
│   ├── AppDatabase.kt               # Room singleton
│   └── TaskRepository.kt            # single source of truth
└── ui/
    ├── DualTrackViewModel.kt        # state + mode filtering
    ├── DualTrackApp.kt              # scaffold, timeline, dialogs
    ├── components/TaskCard.kt
    └── theme/                       # colors, typography, Material3 theme
```

## Planned (not yet implemented)

- Home-screen widget showing today's top Founder + Student task
- Notifications / reminders via WorkManager
- "Weekly intent" ritual screen (one founder goal + one student goal)
- Person/company CRM (investors, professors, recruiters) with follow-up reminders
- Optional cloud sync

## Notes

- Dates are stored as **epoch days** (UTC), so a task stays on the same calendar day
  regardless of timezone.
- Long-press deletes a task immediately — there is no undo yet.
