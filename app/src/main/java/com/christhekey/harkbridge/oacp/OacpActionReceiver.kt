package com.christhekey.harkbridge.oacp

import android.content.Context
import android.content.Intent
import org.oacp.android.OacpParams
import org.oacp.android.OacpReceiver
import org.oacp.android.OacpResult

class OacpActionReceiver : OacpReceiver() {

    override fun onAction(
        context: Context,
        action: String,
        params: OacpParams,
        requestId: String?
    ): OacpResult? {

        return when {
            action.endsWith("ACTION_HOME") -> {
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                OacpResult.success("Home", "Opened home screen")
            }

            action.endsWith("ACTION_BACK") -> {
                OacpResult.error("NOT_IMPLEMENTED", "Back requires the Shizuku execution layer")
            }

            else -> {
                OacpResult.error("UNKNOWN_ACTION", "Unknown action")
            }
        }
    }
}
