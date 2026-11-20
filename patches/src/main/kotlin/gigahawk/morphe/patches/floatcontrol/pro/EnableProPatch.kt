package gigahawk.morphe.patches.floatcontrol.pro

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import gigahawk.morphe.compatibility.Compat

@Suppress("unused")
val enableProPatch =
        bytecodePatch(
                name = "Enable Pro",
                description = "Enables pro features.",
        ) {
                compatibleWith(Compat.FLOAT_CONTROL)

                execute {
                        // Broken on new versions
                        //                        GetTierFingerprint.method.addInstructions(
                        //                                0,
                        //                                """
                        //                 sget-object v0,
                        //
                        // Lcom/floatcontrol/vesc/app/Tier;->PRO:Lcom/floatcontrol/vesc/app/Tier;
                        //                 return-object v0
                        //             """
                        //                        )

                        // Not enough?
                        // Seems to not work, may need to debug to see whats going on?
                        VescAppModelInitFingerprint.method.apply {
                                val moveResults =
                                        VescAppModelInitFingerprint.instructionMatches
                                                .filter {
                                                        getInstruction<Instruction>(it.index)
                                                                .opcode == Opcode.MOVE_RESULT_OBJECT
                                                }
                                                .sortedBy { it.index }
                                moveResults[0].let { match ->
                                        val reg =
                                                getInstruction<OneRegisterInstruction>(match.index)
                                                        .registerA
                                        println("appTier return reg")
                                        println(reg)
                                        replaceInstruction(
                                                match.index,
                                                "const-string v$reg, \"PRO\"",
                                        )
                                }
                                moveResults[1].let { match ->
                                        val reg =
                                                getInstruction<OneRegisterInstruction>(match.index)
                                                        .registerA
                                        println("verified return reg")
                                        println(reg)
                                        replaceInstruction(
                                                match.index,
                                                "const-string v$reg, \"1795167456949\"",
                                        )
                                }
                        }
                }
        }
