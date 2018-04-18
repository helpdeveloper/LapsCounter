package br.com.helpdev.lapscounter.headset

import android.content.*
import android.media.AudioManager
import android.support.v4.content.LocalBroadcastManager
import android.view.KeyEvent
import br.com.helpdev.lapscounter.headset.receiver.HeadsetButtonReceiver

class HeadsetButtonControl {

    private var audioManager: AudioManager? = null
    private var receiverComponent: ComponentName? = null
    private var broadcastButtonReceiver: BroadcastButtonReceiver? = null
    private var headsetButtonControlListener: HeadsetButtonControlListener? = null

    fun registerHeadsetButton(context: Context, headsetButtonControlListener: HeadsetButtonControlListener) {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        receiverComponent = ComponentName(context, HeadsetButtonReceiver::class.java)
        audioManager?.registerMediaButtonEventReceiver(receiverComponent)

        this.headsetButtonControlListener = headsetButtonControlListener
        broadcastButtonReceiver = BroadcastButtonReceiver()
        val filter = IntentFilter()
        filter.addAction(HeadsetButtonReceiver.ACTION_KEYCODE_HEADSET_HOOK)
        filter.addAction(HeadsetButtonReceiver.ACTION_KEYCODE_MEDIA_NEXT)
        filter.addAction(HeadsetButtonReceiver.ACTION_KEYCODE_MEDIA_PREVIOUS)
        filter.addAction(HeadsetButtonReceiver.ACTION_KEYCODE_HEADSET_HOOK_DOUBLE_CLICK)
        filter.addAction(HeadsetButtonReceiver.ACTION_KEYCODE_UNDEFINED)
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastButtonReceiver!!, filter)
    }

    fun unregisterHeadsetButton(context: Context) {
        if (null != receiverComponent) audioManager?.unregisterMediaButtonEventReceiver(receiverComponent)
        if (null != broadcastButtonReceiver) LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastButtonReceiver!!)
    }

    interface HeadsetButtonControlListener {
        fun btHeadsetHookPressed(keyEvent: KeyEvent?)
        fun btHeadsetHookDoubleClickPressed(keyEvent: KeyEvent?)
        fun btHeadsetMediaNextPressed(keyEvent: KeyEvent?)
        fun btHeadsetMediaPreviousPressed(keyEvent: KeyEvent?)
        fun btHeadsetUndefinedPressed(keyEvent: KeyEvent?)
    }

    inner class BroadcastButtonReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (null == headsetButtonControlListener || null == intent) return
            val keyEvent = intent.getParcelableExtra<KeyEvent>(HeadsetButtonReceiver.PARAM_KEY_EVENT)
            when (intent.action) {
                HeadsetButtonReceiver.ACTION_KEYCODE_HEADSET_HOOK -> headsetButtonControlListener!!.btHeadsetHookPressed(keyEvent)
                HeadsetButtonReceiver.ACTION_KEYCODE_MEDIA_NEXT -> headsetButtonControlListener!!.btHeadsetMediaNextPressed(keyEvent)
                HeadsetButtonReceiver.ACTION_KEYCODE_MEDIA_PREVIOUS -> headsetButtonControlListener!!.btHeadsetMediaPreviousPressed(keyEvent)
                HeadsetButtonReceiver.ACTION_KEYCODE_UNDEFINED -> headsetButtonControlListener!!.btHeadsetUndefinedPressed(keyEvent)
                HeadsetButtonReceiver.ACTION_KEYCODE_HEADSET_HOOK_DOUBLE_CLICK -> headsetButtonControlListener!!.btHeadsetHookDoubleClickPressed(keyEvent)
            }
        }
    }

}