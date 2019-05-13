package br.com.helpdev.lapscounter.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import kotlin.concurrent.thread

object AlarmUtils {
    fun alarmAsync(context: Context, rawId: Int) {
        thread {
            MediaPlayer.create(context, rawId).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                } else {
                    setAudioStreamType(AudioManager.STREAM_ALARM)
                }
            }.start()
        }
    }
}