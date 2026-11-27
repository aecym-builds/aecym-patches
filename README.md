# Aecym Patches

Two patches for Muse from Meta (`com.facebook.aura`). Morphe only offers the ones that match the
APK version you loaded, so one bundle covers both.

## Account switcher — Muse 18.0.0.8.161

Muse already contains a full multi-account implementation: parked credentials, an account list and a
stored-token switch. It is disabled by three MobileConfig flags. This patch forces the gate that
reads those flags to report enabled, so the existing feature turns on instead of being rebuilt.

Two ways in:

- **Profile:** open the side panel (leading menu button in the tab header, or edge swipe) and tap your
  name/avatar at the top. The account menu expands.
- **Settings:** open Settings and look at the Accounts card. It has an extra row that is the same
  account menu; tap it to expand the list.

Tap another account to switch. **Add account** logs in to a new account and parks the current one.

Switching goes through the app's own `AuraAccounts.switchTo(userId)`, which leaves the current
session, exchanges the stored token and continues the login. It is not two sessions running at once:
only the active account receives pushes and runs network work.

Caveats:

- Accounts must be parked first. With one login there is nothing to switch to, so use **Add account**
  once from the same menu.
- The token exchange is server-side. If Meta rejects it for this account/build, the switch fails and
  the current session is kept. That cannot be fixed client-side.
- Statically verified against 18.0.0.8.161 and applied cleanly with Morphe 2.4.0, but not yet
  device-tested.
- Muse has no switcher screen of its own in Settings, so the patch adds the existing account menu as a
  row of the Accounts card there. It is the same composable the side-panel header uses, not a new
  screen; if it ever misbehaves, use the side-panel entry.

## Floating bottom bar — Muse 17.1.0.28.160

Custom-colour rounded bar with an animated draggable bubble. Grab the bubble, drag and lift to switch
tabs. Ported from the device-tried 17.0.0.10.160 version, not yet device-tested on 17.1.0.28.160.
Cosmetic glass look, no backdrop blur.

## Add or update in Morphe

Use the repository source: https://github.com/aecym-builds/aecym-patches

Update this source in Morphe to get the current bundle. For a local import, download
`patches-1.0.0.mpp` from the latest release. A previously imported local file does not update itself;
import the new file. No uninstall or app-data clearing is needed just to refresh a patch source.

## No APK

No APK is distributed here and the app is never modified on disk by this repository. The account
switcher changes no credentials, networking or storage formats: it only flips booleans the app
already consults.

## License

GPLv3. See LICENSE and NOTICE files and the included source archive.
