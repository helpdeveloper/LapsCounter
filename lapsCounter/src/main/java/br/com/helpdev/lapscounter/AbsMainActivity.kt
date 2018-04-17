package br.com.helpdev.lapscounter

import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.View
import br.com.helpdev.chronometerlib.Chronometer
import br.com.helpdev.lapscounter.headset.HeadsetButtonControl
import kotlinx.android.synthetic.main.include_buttons.*
import kotlinx.android.synthetic.main.include_chronometer.*
import kotlinx.android.synthetic.main.toolbar.*


abstract class AbsMainActivity : AppCompatActivity() {

    private var chronometer: Chronometer? = null
    private val headsetButtonReceiver = HeadsetButtonControl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        bt_start.setOnClickListener { btStartPressed() }
        bt_resume.setOnClickListener { btResumePressed() }
        bt_lap.setOnClickListener { btLapPressed() }
        bt_pause.setOnClickListener { btPausePressed() }
        bt_save.setOnClickListener { btSavePressed() }
        bt_restart.setOnClickListener { btRestartPressed() }

        if (null == savedInstanceState) {
            chronometer = Chronometer()
        } else {
            chronometer = savedInstanceState.getSerializable("chronometer") as Chronometer
            buttons_frame_2.visibility = savedInstanceState.getInt("buttons_frame_2")
            buttons_lay_restart_save.visibility = savedInstanceState.getInt("buttons_lay_restart_save")
            bt_start.visibility = savedInstanceState.getInt("bt_start")
            bt_lap.visibility = savedInstanceState.getInt("bt_lap")
            bt_pause.visibility = savedInstanceState.getInt("bt_pause")
            bt_resume.visibility = savedInstanceState.getInt("bt_resume")
            layout_chronometer_pause.visibility = savedInstanceState.getInt("layout_chronometer_pause")

            chronometerWidget.base = chronometer!!.getCurrentBase()
            if (View.VISIBLE == bt_pause.visibility) {
                chronometerWidget.start()
            } else {
                chronometerWidgetPause.base = chronometer!!.pauseBaseTime
                chronometerWidgetPause.start()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("chronometer", chronometer)
        outState.putInt("buttons_frame_2", buttons_frame_2.visibility)
        outState.putInt("buttons_lay_restart_save", buttons_lay_restart_save.visibility)
        outState.putInt("bt_start", bt_start.visibility)
        outState.putInt("bt_lap", bt_lap.visibility)
        outState.putInt("bt_pause", bt_pause.visibility)
        outState.putInt("bt_resume", bt_resume.visibility)
        outState.putInt("layout_chronometer_pause", layout_chronometer_pause.visibility)
        super.onSaveInstanceState(outState)
    }

    private fun btStartPressed() {
        btStartResumePressed()
        buttons_frame_2.visibility = View.VISIBLE
        bt_start.visibility = View.GONE
        bt_pause.visibility = View.VISIBLE
    }

    private fun btResumePressed() {
        btStartResumePressed()
        changeLayoutPauseResume(false)
    }

    private fun btStartResumePressed() {
        chronometer!!.start()
        chronometerWidget.base = chronometer!!.getCurrentBase()
        chronometerWidget.start()
    }

    private fun btLapPressed() {
        chronometer!!.lap()
    }

    private fun btPausePressed() {
        chronometer!!.stop()
        chronometerWidget.stop()
        chronometerWidgetPause.base = SystemClock.elapsedRealtime()
        chronometerWidgetPause.start()
        changeLayoutPauseResume(true)
    }

    private fun changeLayoutPauseResume(paused: Boolean) {
        buttons_lay_restart_save.visibility = if (paused) View.VISIBLE else View.GONE
        bt_lap.visibility = if (paused) View.GONE else View.VISIBLE
        bt_pause.visibility = if (paused) View.GONE else View.VISIBLE
        bt_resume.visibility = if (paused) View.VISIBLE else View.GONE
        layout_chronometer_pause.visibility = if (paused) View.VISIBLE else View.INVISIBLE
    }

    private fun btSavePressed() {
        chronometer!!.getObChronometer()
    }

    private fun btRestartPressed() {
        chronometer!!.reset()
        chronometerWidget.base = SystemClock.elapsedRealtime()
        chronometerWidget.stop()
        buttons_frame_2.visibility = View.GONE
        bt_start.visibility = View.VISIBLE
        buttons_lay_restart_save.visibility = View.GONE
        bt_lap.visibility = View.VISIBLE
        bt_pause.visibility = View.GONE
        bt_resume.visibility = View.GONE
        layout_chronometer_pause.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        headsetButtonReceiver.registerHeadsetButton(this)
    }

    override fun onStop() {
        headsetButtonReceiver.unregisterHeadsetButton()
        super.onStop()
    }

    override fun onBackPressed() {
        super.finish()
    }

}
