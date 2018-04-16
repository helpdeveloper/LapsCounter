package br.com.helpdev.lapscounter.headset.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.widget.Toast

class HeadsetButtonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val intentAction = intent.action
        if (Intent.ACTION_MEDIA_BUTTON != intentAction) {
            return
        }
        val event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT) as KeyEvent
        val action = event.action

        if (action == KeyEvent.ACTION_DOWN) {
            val keycode = event.keyCode
            val type = when (keycode) {
                KeyEvent.KEYCODE_MEDIA_NEXT -> "next"
                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> "previous"
                KeyEvent.KEYCODE_HEADSETHOOK -> "hook"
                else -> "null"
            }
            Toast.makeText(context, "BUTTON PRESSED! {$type}", Toast.LENGTH_SHORT).show()
        }
    }

}