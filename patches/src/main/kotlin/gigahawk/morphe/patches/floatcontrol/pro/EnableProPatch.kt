package gigahawk.morphe.patches.floatcontrol.pro

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import gigahawk.morphe.compatibility.Compat

@Suppress("unused")
val enableProPatch =
        bytecodePatch(
                name = "Enable Pro",
                description = "Enables pro features.",
        ) {
          compatibleWith(Compat.FLOAT_CONTROL)

          execute {
            GetTierFingerprint.method.addInstructions(
                    0,
                    """
                 sget-object v0,
            Lcom/floatcontrol/vesc/app/Tier;->PRO:Lcom/floatcontrol/vesc/app/Tier;
                 return-object v0
             """
            )
          }
        }
