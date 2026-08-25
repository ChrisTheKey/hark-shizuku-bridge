package org.oacp.example.test

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.oacp.android.Oacp
import org.oacp.android.OacpParams
import org.oacp.android.OacpResult

/**
 * Foreground OACP actions for the counter.
 *
 * Extends AppCompatActivity (not OacpActivity) because the app uses a
 * Material/AppCompat theme. OacpParams and OacpResult work standalone —
 * you don't need OacpActivity to use the SDK.
 *
 * OacpActivity is convenient when your app uses a plain Activity theme.
 * For AppCompat apps, use this pattern instead.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private var requestId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = getSharedPreferences("oacp_test", MODE_PRIVATE)
        handleIntent(intent)
        updateUI()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val action = intent?.action ?: return
        requestId = intent.getStringExtra(Oacp.EXTRA_REQUEST_ID)
        val params = OacpParams(intent)
        val current = prefs.getInt("counter", 0)

        val newValue = when {
            action.endsWith(".oacp.ACTION_INCREMENT_COUNTER") -> {
                val amount = (params.getInt("amount") ?: 1).coerceIn(1, 100)
                current + amount
            }
            action.endsWith(".oacp.ACTION_DECREMENT_COUNTER") -> {
                val amount = (params.getInt("amount") ?: 1).coerceIn(1, 100)
                current - amount
            }
            action.endsWith(".oacp.ACTION_SET_COUNTER") -> {
                (params.getInt("value") ?: 0).coerceIn(-1000, 1000)
            }
            action.endsWith(".oacp.ACTION_RESET_COUNTER") -> 0
            else -> return
        }

        prefs.edit().putInt("counter", newValue).apply()
        updateUI()

        // Send result back to the assistant
        val rid = requestId ?: return
        val capabilityId = action.substringAfterLast(".ACTION_", "").lowercase()
        OacpResult.success("Counter is now $newValue").send(this, rid, capabilityId)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val counter = prefs.getInt("counter", 0)
        findViewById<TextView>(R.id.counter_value)?.text = counter.toString()
    }
}
