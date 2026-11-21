package app.aecym.muse

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.patch
import app.morphe.patcher.patch.classDefBy
import app.morphe.patcher.string
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference

private const val NAV = "Lcom/facebook/aura/tabs/AuraFixedNavBarKt;"
private const val HELPER = "LX/0C0;"
private const val CLONE = "aecymNavTint60"

private object NavBar : Fingerprint(
    definingClass = NAV,
    name = "AuraFixedNavBar-XiNizjQ",
    returnType = "V",
    filters = listOf(string("hatch-fixed-nav-bar")),
)

private object Background : Fingerprint(
    definingClass = HELPER,
    name = "A0K",
    returnType = "LX/9Tt;",
    parameters = listOf("LX/9N5;", "LX/9Tt;"),
)

/** Version-pinned draft. No credentials, networking, storage or global theme changes. */
@Suppress("unused")
val museTranslucentNavBar = patch(
    name = "Translucent bottom tab bar (experimental)",
    description = "Sets only the Muse bottom tab bar background to 60% opacity. No blur. Untested on a device.",
    default = false,
) {
    compatibleWith(
        Compatibility(
            name = "Muse from Meta",
            packageName = "com.facebook.aura",
            apkFileType = ApkFileType.APK,
            appIconColor = 0x2563EB,
            targets = listOf(AppTarget(version = "17.0.0.10.160", isExperimental = true)),
        ),
    )

    execute {
        val nav = NavBar.method
        val original = Background.originalMethod
        val helperClass = Background.classDef
        check(helperClass.methods.none { it.name == CLONE }) { "Tint helper already exists; use an unpatched APK." }
        val implementation = checkNotNull(original.implementation)
        check(implementation.registerCount == 4) { "Unexpected background helper register layout." }
        val code = implementation.instructions.toList()
        val colorIndex = code.indexOfFirst {
            val field = (it as? ReferenceInstruction)?.reference as? FieldReference
            it.opcode == Opcode.IGET_WIDE && field?.definingClass ==
                "Lcom/facebook/aura/vds/compose/theme/AuraColors;" && field.name == "background" && field.type == "J"
        }
        check(colorIndex >= 0) { "Theme background read not found." }
        val colorRead = code[colorIndex] as TwoRegisterInstruction
        check(colorRead.registerA == 1 && colorRead.registerB == 0) { "Unexpected color registers." }
        check(code[colorIndex + 1].opcode == Opcode.SGET_OBJECT &&
            (code[colorIndex + 1] as? com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction)?.registerA == 0
        ) { "Expected disposable v0 before the shape load." }
        val alphaClass = classDefBy("LX/AH4;")
        check(alphaClass.methods.any { it.name == "A04" && it.returnType == "J" &&
            it.parameterTypes.map { p -> p.toString() } == listOf("J", "F")
        }) { "Expected packed-color alpha utility is missing." }

        val calls = nav.implementation!!.instructions.withIndex().filter { (_, instruction) ->
            val reference = (instruction as? ReferenceInstruction)?.reference as? MethodReference
            instruction.opcode == Opcode.INVOKE_STATIC && reference?.definingClass == HELPER &&
                reference.name == "A0K" && reference.parameterTypes.map { it.toString() } == listOf("LX/9N5;", "LX/9Tt;")
        }
        check(calls.size == 1) { "Expected exactly one tab-bar background call, found ${calls.size}." }
        val (callIndex, instruction) = calls.single()
        val call = instruction as com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
        check(call.registerCount == 2) { "Unexpected call arity." }

        // Copy at patch time from the user's APK. No proprietary app implementation is shipped here.
        // Only the new private-to-this-use clone receives the alpha change; the shared helper is untouched.
        val tinted = MutableMethod(original)
        tinted.setName(CLONE)
        tinted.addInstructions(
            colorIndex + 1,
            """
                const v0, 0x3f19999a
                invoke-static {v1, v2, v0}, LX/AH4;->A04(JF)J
                move-result-wide v1
            """,
        )
        helperClass.methods.add(tinted)
        nav.replaceInstruction(
            callIndex,
            BuilderInstruction35c(
                Opcode.INVOKE_STATIC, 2, call.registerC, call.registerD, 0, 0, 0,
                ImmutableMethodReference(HELPER, CLONE, listOf("LX/9N5;", "LX/9Tt;"), "LX/9Tt;"),
            ),
        )
    }
}
