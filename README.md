# Muse Glass Navbar

Experimental Morphe patch source for Muse from Meta. **Compiled successfully with GitHub Actions. Not applied to an APK or tested on a device.**

## Download on your phone

1. Download [patches-1.0.0.mpp](https://github.com/aecym-builds/muse-glass-navbar/releases/download/experimental-1/patches-1.0.0.mpp) from the [experimental release](https://github.com/aecym-builds/muse-glass-navbar/releases/tag/experimental-1). No PC is needed for this download.
2. Import the downloaded `.mpp` as a local bundle in a compatible Morphe Manager/CLI. The repository homepage is not a Morphe patch-source URL.
3. Use your own original Muse `17.0.0.10.160` APK, select the experimental translucent bottom tab bar patch, and back up app data before installing a patched APK.

[Successful cloud build](https://github.com/aecym-builds/muse-glass-navbar/actions/runs/44918819343). Bundle SHA-256: `5267f174707028b5eff441dfddceb53bc90daadeed263dcc6756efd33ece2869`. Compilation is verified, runtime behavior and appearance are not.

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

[`2-TranslucentNavBarPatch.kt`](2-TranslucentNavBarPatch.kt) is a convenient readable copy. [`3-muse-glass-navbar-source.zip`](3-muse-glass-navbar-source.zip) contains the complete Gradle project; the authoritative source inside it is `patches/src/main/kotlin/app/aecym/muse/TranslucentNavBarPatch.kt`.

1. Download and unzip `3-muse-glass-navbar-source.zip`.
2. Install JDK 21 and set `JAVA_HOME` to it.
3. GitHub Packages access is required by the official Morphe build configuration. Set `GITHUB_ACTOR` to your GitHub username and `GITHUB_TOKEN` to a token with `read:packages` permission, using your local secret manager or environment. Never commit a token. The equivalent Gradle properties are `gpr.user` and `gpr.key` in your private user Gradle configuration, not this project.
4. In the extracted project, run:

```sh
chmod +x gradlew
./gradlew :patches:buildAndroid --no-daemon
```

Expected output after a successful build: `patches/build/libs/patches-*.mpp`. A raw Kotlin file cannot be loaded as a compiled patch bundle.

This project follows the official Morphe patches template: settings plugin `2.1.1`, patcher catalog `2.5.0`, and its checked-in Gradle wrapper. The initial local attempt stopped at plugin resolution before compiling Kotlin. The subsequent GitHub Actions build succeeded using its built-in package-read token. The release bundle was downloaded and its archive integrity and patch manifest were checked. The source ZIP preserves the initial source-only notes; this README records the newer build result.

## Testing checklist

Use your own unmodified APK of the exact target version and back up app data first. Build the `.mpp`, then load the local bundle using a compatible Morphe Manager/CLI version. Select only this experimental patch. Record build errors rather than disabling its safety checks. Confirm the app launches, the bottom tabs still work, and light/dark themes remain readable. Screenshots of both themes are needed before calling the visual result verified.

Patching changes the APK signature. Installing over the official app may fail or require uninstalling it, which can erase local data. A modified app may crash or stop working. Keep an unmodified copy available. No patched APK is distributed.

## Provenance and license

Build scaffolding comes from [MorpheApp/morphe-patches-template](https://github.com/MorpheApp/morphe-patches-template). See [LICENSE](4-LICENSE) and [NOTICE](5-NOTICE) for the GPL-3.0 license and upstream notices. The patch is original source and contains no APK, decompiled app implementation, or smali dump. Runtime helper code is copied only from the user's own APK during patch execution.

Not affiliated with Meta or Morphe.
