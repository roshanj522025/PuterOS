# Puter Android App

A native Android wrapper for [puter.com](https://puter.com) built with **Capacitor 6**.  
Features a custom login screen, an in-app WebView (no system browser), and a GitHub Actions pipeline for automated APK builds.

---

## Project Structure

```
puter-android/
├── src/
│   └── index.html          # Login screen + in-app WebView shell
├── android/
│   ├── app/
│   │   └── src/main/
│   │       ├── java/com/puter/app/
│   │       │   ├── MainActivity.java
│   │       │   └── MainApplication.java
│   │       ├── res/
│   │       │   ├── values/  (colors, strings, styles)
│   │       │   ├── drawable/ (splash icon)
│   │       │   └── xml/     (network config, file paths)
│   │       └── AndroidManifest.xml
│   ├── build.gradle
│   ├── settings.gradle
│   ├── variables.gradle
│   └── gradle.properties
├── .github/
│   └── workflows/
│       └── build.yml       # CI/CD pipeline
├── capacitor.config.json
├── package.json
└── .gitignore
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Node.js | 20+ |
| npm | 10+ |
| Java JDK | 17 |
| Android Studio | Hedgehog+ |
| Android SDK | API 34 |

---

## Local Setup

```bash
# 1. Install dependencies
npm install

# 2. Sync web assets into the Android project
npx cap sync android

# 3. Open in Android Studio (optional)
npx cap open android

# 4. Or build directly via Gradle
cd android && ./gradlew assembleDebug
```

The debug APK will be at:
```
android/app/build/outputs/apk/debug/app-debug.apk
```

---

## Generating a Signing Keystore (release builds)

```bash
keytool -genkey -v \
  -keystore puter-release.jks \
  -alias puter \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

Then base64-encode it for GitHub Secrets:
```bash
base64 -i puter-release.jks | pbcopy   # macOS
base64 -w 0 puter-release.jks          # Linux
```

---

## GitHub Actions Secrets

Add these in **Settings → Secrets and variables → Actions**:

| Secret | Description |
|--------|-------------|
| `KEYSTORE_BASE64` | Base64-encoded `.jks` keystore |
| `KEYSTORE_PASSWORD` | Keystore password |
| `KEY_ALIAS` | Key alias (e.g. `puter`) |
| `KEY_PASSWORD` | Key password |

---

## CI/CD Workflow

| Trigger | Build |
|---------|-------|
| Push to `main` / `develop` | Debug APK |
| Pull Request | Debug APK |
| Push tag `v*` | Signed release APK + GitHub Release |
| Manual dispatch | Debug or Release (your choice) |

---

## How Login Works

1. The app opens to a **custom login screen** (`src/index.html`).
2. On submit, credentials are posted to `https://api.puter.com/login`.
3. On success, the auth token is injected as a query param and Puter loads inside an `<iframe>` that fills the screen — **no system browser is ever opened**.
4. "Continue as Guest" skips auth and loads Puter directly.

---

## Capacitor Plugins Used

- `@capacitor/android` — core bridge
- `@capacitor/splash-screen` — branded splash
- `@capacitor/status-bar` — dark status bar to match the UI
