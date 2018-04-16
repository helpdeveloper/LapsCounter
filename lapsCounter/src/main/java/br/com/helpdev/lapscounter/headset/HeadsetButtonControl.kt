package br.com.helpdev.lapscounter.headset

import android.content.ComponentName
import android.content.Context
import android.media.AudioManager
import br.com.helpdev.lapscounter.headset.receiver.HeadsetButtonReceiver

class HeadsetButtonControl {

    private var audioManager: AudioManager? = null
    private var receiverComponent: ComponentName? = null

    fun registerHeadsetButton(context: Context) {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        receiverComponent = ComponentName(context, HeadsetButtonReceiver::class.java)
        audioManager?.registerMediaButtonEventReceiver(receiverComponent)
    }

    fun unregisterHeadsetButton() {
        if (null != receiverComponent) audioManager?.unregisterMediaButtonEventReceiver(receiverComponent)
    }

}