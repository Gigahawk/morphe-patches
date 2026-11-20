package gigahawk.morphe.patches.floatcontrol.pro

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.anyInstruction
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.opcode
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object GetTierFingerprint :
        Fingerprint(
                definingClass = "Lcom/floatcontrol/vesc/app/SettingsState;",
                name = "getTier",
                accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
                returnType = "Lcom/floatcontrol/vesc/app/Tier;",
                parameters = emptyList(),
        )

object VescAppModelInitFingerprint :
        Fingerprint(
                name = "<init>",
                filters =
                        listOf(
                                // SettingsStore.getString
                                string("appTier"),
                                methodCall(
                                        definingClass = "L",
                                        parameters =
                                                listOf("Ljava/lang/String;", "java/lang/String;")
                                ),
                                opcode(
                                        Opcode.MOVE_RESULT_OBJECT,
                                        InstructionLocation.MatchAfterImmediately()
                                ),

                                // SettingsStore.getString
                                string("tierVerifiedAtMs"),
                                methodCall(
                                        definingClass = "L",
                                        parameters =
                                                listOf("Ljava/lang/String;", "Ljava/lang/String;")
                                ),
                                opcode(
                                        Opcode.MOVE_RESULT_OBJECT,
                                        InstructionLocation.MatchAfterImmediately()
                                ),

                                // Load default fallback tier
                                fieldAccess(opcode = Opcode.SGET_OBJECT, type = "L", name = "FREE"),

                                // Check if non free tier is valid
                                opcode(Opcode.IF_EQ),
                                opcode(Opcode.IF_EQZ),
                                anyInstruction(
                                        methodCall(
                                                definingClass = "Ljava/lang/System",
                                                name = "currentTimeMillis",
                                                parameters = listOf(),
                                        ),
                                        methodCall(
                                                definingClass = "L",
                                                name = "currentEpochMillis",
                                                parameters = listOf(),
                                        ),
                                ),
                                methodCall(
                                        definingClass = "Ljava/lang/Long",
                                        name = "longValue",
                                ),
                                opcode(Opcode.IF_LEZ),
                        )
        )
