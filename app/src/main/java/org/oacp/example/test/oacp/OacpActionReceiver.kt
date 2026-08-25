package org.oacp.example.test.oacp

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import org.oacp.android.OacpParams
import org.oacp.android.OacpReceiver
import org.oacp.android.OacpResult

/**
 * Handles background OACP actions — these do NOT open the app.
 *
 * - get_battery: returns battery percentage
 * - get_counter: returns current counter value
 *
 * Foreground actions (increment/decrement/reset) are handled by MainActivity
 * via activity intent filters — Hark calls startActivity() directly.
 */
class OacpActionReceiver : OacpReceiver() {

    override fun onAction(
        context: Context,
        action: String,
        params: OacpParams,
        requestId: String?
    ): OacpResult? {
        return when {
            action.endsWith(".oacp.ACTION_GET_BATTERY") -> {
                val level = getBatteryLevel(context)
                OacpResult.success("Battery is at $level%")
            }
            action.endsWith(".oacp.ACTION_GET_COUNTER") -> {
                val prefs = context.getSharedPreferences("oacp_test", Context.MODE_PRIVATE)
                val counter = prefs.getInt("counter", 0)
                OacpResult.success("Counter is at $counter")
            }
            else -> null
        }
    }

    private fun getBatteryLevel(context: Context): Int {
        val batteryStatus = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
        return if (scale > 0) (level * 100) / scale else -1
    }
}
