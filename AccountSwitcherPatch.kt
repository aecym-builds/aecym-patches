package app.aecym.muse

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.mutableClassDefBy
import app.morphe.patcher.patch.patch

private const val GATE = "Lcom/facebook/aura/auth/login/credentials/AuraRetainedAccountLoginGate;"

/**
 * Muse ships a complete multi-account implementation (parked credentials, account list,
 * stored-token switch) behind three MobileConfig flags that are off for normal users.
 *
 * Every one of those flags is read through this single gate class, so forcing the gate
 * open enables the existing feature instead of reimplementing it:
 *
 *  - isEnabled()                      -> enable_retained_account_login
 *  - getFastAccountSwitchingEnabled() -> enable_fast_account_switching
 *  - isStoredTokenSwitchEnabled()     -> enable_stored_token_switch
 *  - isAccountSwitcherEnabled(...)    -> the combined check the account menu reads
 *
 * No new UI is injected: the account list is the side-panel header menu the app already
 * renders (AuraAccountMenu), and a tap calls AuraAccounts.switchTo(userId).
 */
@Suppress("unused")
val museAccountSwitcher = patch(
    name = "Account switcher",
    description = "Unlocks Muse's built-in account switcher. The side-panel account header then lists every " +
        "account parked on this device, and tapping one switches to it without logging out first. " +
        "Add account from the same menu to park another login. Client-side only: if Meta's server " +
        "rejects the stored-token exchange, the switch fails and the current session is kept.",
    default = true,
) {
    compatibleWith(
        Compatibility(
            name = "Muse from Meta",
            packageName = "com.facebook.aura",
            apkFileType = ApkFileType.APK,
            appIconColor = 0x2563EB,
            targets = listOf(AppTarget(version = "18.0.0.8.161")),
        ),
    )

    execute {
        val gate = mutableClassDefBy(GATE)

        listOf(
            "isEnabled",
            "getFastAccountSwitchingEnabled",
            "isStoredTokenSwitchEnabled",
            "isAccountSwitcherEnabled",
        ).forEach { name ->
            val method = gate.methods.firstOrNull { it.name == name && it.returnType == "Z" }
                ?: error("$GATE->$name()Z is missing. This Muse build is not supported.")
            checkNotNull(method.implementation) { "$GATE->$name has no body." }
            method.addInstructions(
                0,
                """
                    const/4 v0, 0x1
                    return v0
                """,
            )
        }
    }
}
