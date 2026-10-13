package gigahawk.morphe.patches.floatcontrol.pro

import app.morphe.patcher.Fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object GetTierFingerprint :
        Fingerprint(
                definingClass = "Lcom/floatcontrol/vesc/app/SettingsState;",
                name = "getTier",
                accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
                returnType = "Lcom/floatcontrol/vesc/app/Tier;",
                parameters = emptyList(),
        )
