package gigahawk.morphe.compatibility

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Compat {
  val FLOAT_CONTROL =
          Compatibility(
                  name = "Float Control",
                  packageName = "com.floatcontrol",
                  targets =
                          listOf(
                                  AppTarget("3.6.8"),
                          ),
          )
}
