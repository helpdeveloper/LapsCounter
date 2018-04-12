package br.com.helpdev.poollapscounter

import android.content.ComponentName
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import br.com.helpdev.poollapscounter.receiver.HeadsetButtonReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var audioManager: AudioManager? = null
    private var receiverComponent: ComponentName? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        list_view.emptyView = text_view_empty
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun registerHeadsetButton() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        receiverComponent = ComponentName(this, HeadsetButtonReceiver::class.java)
        audioManager?.registerMediaButtonEventReceiver(receiverComponent)
    }

    override fun onResume() {
        super.onResume()
        registerHeadsetButton()
    }

    override fun onStop() {
        if (null != receiverComponent) audioManager?.unregisterMediaButtonEventReceiver(receiverComponent)
        super.onStop()
    }

    override fun onBackPressed() {
        super.finish()
    }

}
