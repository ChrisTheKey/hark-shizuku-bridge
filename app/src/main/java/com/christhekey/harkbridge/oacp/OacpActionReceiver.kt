package com.christhekey.harkbridge.oacp

import android.content.Context
import android.content.pm.PackageManager
import org.oacp.android.OacpParams
import org.oacp.android.OacpReceiver
import org.oacp.android.OacpResult
import rikka.shizuku.Shizuku

class OacpActionReceiver : OacpReceiver() {
    override fun onAction(
        context: Context,
        action: String,
        params: OacpParams,
        requestId: String?
    ): OacpResult? {
        if (!Shizuku.pingBinder())
            return OacpResult.error("Shizuku is not running")

        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED)
            return OacpResult.error("Shizuku permission missing")

        return try {
            when {
                action.endsWith("ACTION_HOME") ->
                    run("input", "keyevent", "3")

                action.endsWith("ACTION_BACK") ->
                    run("input", "keyevent", "4")

                action.endsWith("ACTION_TAP") -> {
                    val x = params.getInt("x") ?: return OacpResult.error("Missing x")
                    val y = params.getInt("y") ?: return OacpResult.error("Missing y")
                    run("input", "tap", "$x", "$y")
                }

                action.endsWith("ACTION_SWIPE") -> {
                    val x1 = params.getInt("x1") ?: 500
                    val y1 = params.getInt("y1") ?: 1500
                    val x2 = params.getInt("x2") ?: 500
                    val y2 = params.getInt("y2") ?: 500
                    run("input", "swipe", "$x1", "$y1", "$x2", "$y2", "350")
                }

                action.endsWith("ACTION_INPUT_TEXT") -> {
                    val text = params.getString("text")
                        ?: return OacpResult.error("Missing text")
                    run("input", "text", text.replace(" ", "%s"))
                }

                action.endsWith("ACTION_OPEN_APP") -> {
                    val pkg = params.getString("package")
                        ?: return OacpResult.error("Missing package")
                    run("monkey", "-p", pkg, "-c
, "android.intent.category.LAUNCHER", "1")
                }

                else -> return OacpResult.error("Unknown action")
            }
        } catch (e: Exception) {
            OacpResult.error(e.message ?: "Execution failed")
        }
    }

    @Suppress("DEPRECATION")
    private fun run(vararg cmd: String): OacpResult {
        val process = Shizuku.newProcess(cmd, null, null)
        val output = process.inputStream.bufferedReader().readText()
        val error = process.errorStream.bufferedReader().readText()
        val code = process.waitFor()

        return if (code == 0) {
            OacpResult.success(
                if (output.isBlank()) "Done" else output.trim()
            )
        } else {
            OacpResult.error(
                if (error.isBlank()) "Command failed" else error.trim()
            )
        }
    }
}
