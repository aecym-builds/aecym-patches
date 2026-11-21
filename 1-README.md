# Muse Glass Navbar

Experimental Morphe patch source for Muse from Meta. **Not compiled, not applied to an APK, and not tested on a device.** No installable `.mpp` bundle is provided yet. This repository link is not a Morphe patch-source URL.

## Intended change

Set the fixed bottom tab bar background to 60% opacity while keeping the current theme color. This is tint/transparency only, not backdrop blur or an iOS glass effect. Parent backgrounds or layout may mean little or no visible change.

- Target package: `com.facebook.aura`
- Target version: `17.0.0.10.160`
- Analyzed version code: `1062201702`
- Opt-in patch, disabled by default
- Only one tab-bar background call is redirected to a new helper cloned at patch time. The shared background helper is not changed.
- No authentication, session, network, storage, or global theme changes.

The code has version-specific fingerprints and structural checks. Other versions are unsupported. A matching version label does not prove compatibility with a different APK variant.

## Read or build

`TranslucentNavBarPatch.kt` is a convenient readable copy. `muse-glass-navbar-source.zip` contains the complete Gradle project; the authoritative source inside it is `patches/src/main/kotlin/app/aecym/muse/TranslucentNavBarPatch.kt`.

1. Download and unzip `muse-glass-navbar-source.zip`.
2. Install JDK 21 and set `JAVA_HOME` to it.
3. GitHub Packages access is required by the official Morphe build configuration. Set `GITHUB_ACTOR` to your GitHub username and `GITHUB_TOKEN` to a token with `read:packages` permission, using your local secret manager or environment. Never commit a token. The equivalent Gradle properties are `gpr.user` and `gpr.key` in your private user Gradle configuration, not this project.
4. In the extracted project, run:

```sh
chmod +x gradlew
./gradlew :patches:buildAndroid --no-daemon
```

Expected output after a successful build: `patches/build/libs/patches-*.mpp`. A raw Kotlin file cannot be loaded as a compiled patch bundle.

This project follows the official Morphe patches template: settings plugin `2.1.1`, patcher catalog `2.5.0`, and its checked-in Gradle wrapper. The initial local attempt stopped at plugin resolution before compiling Kotlin. Package authentication is configured by the template; an authenticated build has not been verified here. Build or API fixes may still be needed.

## Testing checklist

Use your own unmodified APK of the exact target version and back up app data first. Build the `.mpp`, then load the local bundle using a compatible Morphe Manager/CLI version. Select only this experimental patch. Record build errors rather than disabling its safety checks. Confirm the app launches, the bottom tabs still work, and light/dark themes remain readable. Screenshots of both themes are needed before calling the visual result verified.

Patching changes the APK signature. Installing over the official app may fail or require uninstalling it, which can erase local data. A modified app may crash or stop working. Keep an unmodified copy available. No patched APK is distributed.

## Provenance and license

Build scaffolding comes from [MorpheApp/morphe-patches-template](https://github.com/MorpheApp/morphe-patches-template). See LICENSE and NOTICE for the GPL-3.0 license and upstream notices. The patch is original source and contains no APK, decompiled app implementation, or smali dump. Runtime helper code is copied only from the user's own APK during patch execution.

Not affiliated with Meta or Morphe.
