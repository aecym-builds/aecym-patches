package app.aecym.muse

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.mutableClassDefBy
import app.morphe.patcher.patch.patch
import com.android.tools.smali.dexlib2.Opcode

private const val GATE = "Lcom/facebook/aura/auth/login/credentials/AuraRetainedAccountLoginGate;"
private const val SETTINGS_SECTION =
    "Lcom/facebook/aura/settings/HatchSettingsScreenKt\$AccountsCenterSection\$3\$1;"

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
 * The account list is the app's own `AuraAccountMenu` (avatar + name + dropdown) and a tap
 * calls `AuraAccounts.switchTo(userId)`. That menu lives in the side-panel header. Muse has
 * no switcher in the Settings screen, so the same menu is added as an extra row of the
 * Accounts card there, which is the only account surface Settings has.
 */
@Suppress("unused")
val museAccountSwitcher = patch(
    name = "Account switcher",
    description = "Unlocks Muse's built-in account switcher. The side-panel account header lists every " +
        "account parked on this device and tapping one switches to it without logging out first, and the " +
        "same menu is added to the Accounts card in Settings. Add account from either menu to park another " +
        "login. Client-side only: if Meta's server rejects the stored-token exchange, the switch fails and " +
        "the current session is kept.",
    default = true,
) {
    // Small composable that renders the app's own account menu; added to Settings below.
    extendWith("account-menu-slot.mpe")

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

        // Settings entry: the Accounts card is built from HatchSectionScope items. Add one more
        // item that renders AuraAccountMenu, so Settings has a switcher too.
        val section = mutableClassDefBy(SETTINGS_SECTION)
        val addItems = section.methods.firstOrNull {
            it.name == "invoke" &&
                it.parameterTypes.size == 1 &&
                it.parameterTypes.first().toString() == "Lcom/facebook/aura/ui/HatchSectionScope;"
        } ?: error("$SETTINGS_SECTION->invoke(HatchSectionScope) is missing. This Muse build is not supported.")
        val body = checkNotNull(addItems.implementation) { "$SETTINGS_SECTION->invoke has no body." }
        val last = body.instructions.count() - 1
        check(last >= 0 && body.instructions[last].opcode == Opcode.RETURN_VOID) {
            "$SETTINGS_SECTION->invoke does not end in return-void; refusing to guess an insertion point."
        }
        addItems.addInstructions(
            last,
            """
                new-instance v1, Lapp/aecym/muse/AccountMenuSlot;
                invoke-direct {v1}, Lapp/aecym/muse/AccountMenuSlot;-><init>()V
                const v0, -0x1d3cc02e
                invoke-static {p1, v1, v0}, LX/007;->A1F(Lcom/facebook/aura/ui/HatchSectionScope;Ljava/lang/Object;I)V
            """,
        )
    }
}
