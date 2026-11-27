# Aecym Patches

A custom-colour floating bottom bar for Muse from Meta, with an animated bubble you can grab and drag. Lift to switch tabs without opening intermediate tabs.

## Add or update in Morphe

Use the same repository source: https://github.com/aecym-builds/aecym-patches

Update this source in Morphe to get the current bundle. It contains one patch, **Floating bottom bar**, selected by default for the compatible app. Keep the **Navbar color** option, or choose another colour before patching. Repatch to change colour.

For a local import, download `patches-1.0.0.mpp` from the latest release on this repository. A previously imported local file does not update itself; import the new file. No uninstall or app-data clearing is needed just to refresh a patch source. A locally stored source label may keep its previous name; this bundle's embedded name is Aecym Patches.

## Compatibility and testing

- Muse from Meta, package `com.facebook.aura`, version **17.1.0.28.160** only.
- The earlier 17.0.0.10.160 version of this implementation was tried on one user's phone. This 17.1.0.28.160 port is statically checked against the app but not yet device-tested. Recordings of the earlier version show the rounded bar, animated bubble and drag-to-select working. This is limited device testing, not broad stability validation.
- A small backward wobble can occur as a drag settles. Long presses, cancellation and history swipes were not separately verified in those recordings.
- The glass-look drawing is cosmetic. It does not provide true backdrop blur or optical refraction.
- No authentication, session, networking or storage changes. No APK is distributed.

Old diagnostic patches are not offered in the current bundle. Historic releases remain available as fallbacks. The workflow retains the exact drawing and gesture implementation from bundle 6, then strips the obsolete patch entries and updates metadata before compilation.

## License

GPLv3. See LICENSE and NOTICE files and the included source archive.

For Muse 17.0.0.10.160 use the older bundle-7 release.
