package br.com.helpdev.lapscounter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.helpdev.chronometerlib.Chronometer
import br.com.helpdev.lapscounter.adapter.LapsAdapter
import br.com.helpdev.lapscounter.headset.HeadsetButtonControl
import br.com.helpdev.lapscounter.utils.AlarmUtils.alarmAsync
import br.com.helpdev.lapscounter.utils.ChronometerUtils
import br.com.helpdev.lapscounter.utils.ChronometerShareUtils
import kotlinx.android.synthetic.main.include_buttons.*
import kotlinx.android.synthetic.main.include_chronometer.*
import kotlinx.android.synthetic.main.include_lap_log.*
import kotlinx.android.synthetic.main.lap_log_chronometers.*
import kotlinx.android.synthetic.main.main_toolbar.*


abstract class AbsMainActivity : AppCompatActivity(), HeadsetButtonControl.HeadsetButtonControlListener {
    companion object {
        private const val REQUEST_SETTINGS = 15
    }

    private lateinit var chronometer: Chronometer
    private var headsetButtonReceiver: HeadsetButtonControl? = null
    private var audioEnable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        configureOnClicks()
        if (null == savedInstanceState) {
            chronometer = Chronometer()
        } else {
            chronometer = savedInstanceState.getSerializable("chronometer") as Chronometer
            restoreVisibilities(savedInstanceState)
            onRestoreChronometer()
        }
        updateParameters()
    }

    private fun configureOnClicks() {
        bt_start.setOnClickListener { btStartPressed().run { alarm(R.raw.beep) } }
        bt_resume.setOnClickListener { btResumePressed().run { alarm(R.raw.beep) } }
        bt_lap.setOnClickListener { btLapPressed().run { alarm(R.raw.beep_2) } }
        bt_pause.setOnClickListener { btPausePressed().run { alarm(R.raw.beep) } }
        bt_share.setOnClickListener { btSharePressed() }
        bt_save.setOnClickListener { btSavePressed() }
        bt_restart.setOnClickListener { btRestartPressed() }
        bt_stop.setOnClickListener { btStopPressed() }
    }

    private fun onRestoreChronometer() {
        refreshBasesTotal()
        checkChronometerStatus()
        refreshChronometerLog()
        createAdapter()
    }

    private fun checkChronometerStatus() {
        when {
            Chronometer.STATUS_STARTED == chronometer.status -> {
                startChronometersWidget()
                if ((SystemClock.elapsedRealtime() - chronometer.getLastLapBasePause()) > 1) {
                    chronometerLogPause.setTextColor(Color.RED)
                    chronometerLogPause.base = chronometer.getLastLapBasePause()
                }
            }
            Chronometer.STATUS_PAUSED == chronometer.status -> startChronometersPause()
            Chronometer.STATUS_STOPPED == chronometer.status -> refreshBasesPaused()
        }
    }

    private fun restoreVisibilities(savedInstanceState: Bundle) {
        buttons_frame_2.visibility = savedInstanceState.getInt("buttons_frame_2")
        buttons_lay_restart_save.visibility = savedInstanceState.getInt("buttons_lay_restart_save")
        audioEnable = savedInstanceState.getBoolean("audioEnable")
        bt_start.visibility = savedInstanceState.getInt("bt_start")
        bt_lap.visibility = savedInstanceState.getInt("bt_lap")
        bt_pause.visibility = savedInstanceState.getInt("bt_pause")
        bt_resume.visibility = savedInstanceState.getInt("bt_resume")
        bt_stop.visibility = savedInstanceState.getInt("bt_stop")
        layout_chronometer_pause.visibility = savedInstanceState.getInt("layout_chronometer_pause")
        chronometer_lap_log.visibility = savedInstanceState.getInt("chronometer_lap_log")
        text_view_empty.visibility = savedInstanceState.getInt("text_view_empty")
    }

    private fun updateParameters() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val infoLapValue = sp.getFloat(getString(R.string.pref_lap_distance_name), resources.getInteger(R.integer.pref_lap_distance_default_value).toFloat())
        audioEnable = sp.getBoolean(getString(R.string.pref_audio_click_name), resources.getBoolean(R.bool.pref_audio_click_value))
        info_lap.text = getString(R.string.info_distance_lap, infoLapValue)
        updateInfoTravelled()
    }

    private fun updateInfoTravelled() {
        val distance = ChronometerUtils.getDistanceTravelled(this, chronometer)
        info_travelled.text = getString(R.string.info_travelled, distance)
        val pace = ChronometerUtils.getPace(this, chronometer)
        info_pace.text = getString(R.string.info_pace, pace.first, pace.second)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_SETTINGS == requestCode) {
            updateParameters()
        }
    }

    private fun createAdapter() {
        recycler_view.adapter = LapsAdapter(this, chronometer.getObChronometer())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("chronometer", chronometer)
        outState.putInt("buttons_frame_2", buttons_frame_2.visibility)
        outState.putInt("buttons_lay_restart_save", buttons_lay_restart_save.visibility)
        outState.putInt("bt_start", bt_start.visibility)
        outState.putInt("bt_stop", bt_stop.visibility)
        outState.putInt("bt_lap", bt_lap.visibility)
        outState.putInt("bt_pause", bt_pause.visibility)
        outState.putInt("bt_resume", bt_resume.visibility)
        outState.putInt("layout_chronometer_pause", layout_chronometer_pause.visibility)
        outState.putInt("chronometer_lap_log", chronometer_lap_log.visibility)
        outState.putInt("text_view_empty", text_view_empty.visibility)
        outState.putBoolean("audioEnable", audioEnable)
        super.onSaveInstanceState(outState)
    }

    private fun btStopPressed() {
        bt_stop.visibility = View.GONE
        bt_resume.visibility = View.GONE
        layout_chronometer_pause.visibility = View.GONE
        buttons_lay_restart_save.visibility = View.VISIBLE
        chronometer.stop()
        chronometerWidgetPause.stop()
        chronometerLogPause.stop()
        refreshBasesPaused()
        updateInfoTravelled()
    }

    private fun btStartPressed() {
        createAdapter()
        btStartResumePressed()
        buttons_frame_2.visibility = View.VISIBLE
        bt_start.visibility = View.GONE
        bt_pause.visibility = View.VISIBLE
        chronometer_lap_log.visibility = View.VISIBLE
        text_view_empty.visibility = View.GONE
        refreshChronometerLog()
    }

    private fun btResumePressed() {
        btStartResumePressed()
        changeLayoutPauseResume(false)
        chronometerLogPause.stop()
        chronometerWidgetPause.stop()
    }

    private fun btStartResumePressed() {
        chronometer.start()
        refreshBasesTotal()
        startChronometersWidget()
        refreshChronometerLog()
    }

    private fun btPausePressed() {
        chronometer.pause()
        chronometerWidget.stop()
        chronometerLogWidget.stop()
        chronometerLogCurrent.stop()
        startChronometersPause()
        changeLayoutPauseResume(true)
    }

    private fun btLapPressed() {
        chronometer.lap()
        chronometerLogPause.base = SystemClock.elapsedRealtime()
        chronometerLogPause.setTextColor(ContextCompat.getColor(this, R.color.colorSecondaryText))
        refreshChronometerLog()
        smoothScrollToPosition()
        updateInfoTravelled()
    }

    private fun smoothScrollToPosition() {
        recycler_view.also { recycler ->
            recycler.adapter?.apply {
                notifyDataSetChanged()
                recycler.smoothScrollToPosition(itemCount)
            }
        }
    }


    private fun alarm(rawId: Int) {
        if (!audioEnable) return
        alarmAsync(this, rawId)
    }

    private fun refreshBasesTotal() {
        chronometerWidget.base = chronometer.getCurrentBase()
        chronometerLogWidget.base = chronometer.getCurrentBase()
    }

    private fun refreshBasesPaused() {
        chronometerWidgetPause.base = chronometer.getPauseBaseTime()
        chronometerLogPause.base = chronometer.getLastLapBasePause()
    }

    private fun startChronometersWidget() {
        chronometerWidget.start()
        chronometerLogWidget.start()
        chronometerLogCurrent.start()
    }

    private fun refreshChronometerLog() {
        numberOfLap.text = getString(R.string.num_lap, String.format("%02d", chronometer.getObChronometer().laps.size))
        chronometerLogCurrent.base = chronometer.getBaseLastLap()
    }

    private fun startChronometersPause() {
        refreshBasesPaused()
        chronometerLogPause.setTextColor(Color.RED)
        chronometerWidgetPause.start()
        chronometerLogPause.start()
    }

    private fun changeLayoutPauseResume(paused: Boolean) {
        buttons_lay_restart_save.visibility = View.GONE
        bt_stop.visibility = if (paused) View.VISIBLE else View.GONE
        bt_lap.visibility = if (paused) View.GONE else View.VISIBLE
        bt_pause.visibility = if (paused) View.GONE else View.VISIBLE
        bt_resume.visibility = if (paused) View.VISIBLE else View.GONE
        layout_chronometer_pause.visibility = if (paused) View.VISIBLE else View.INVISIBLE
    }

    private fun btSavePressed() {
        //TODO - SAVE
    }

    private fun btSharePressed() {
        ChronometerShareUtils.shareText(this, chronometer)
    }

    private fun btRestartPressed() {
        chronometer.reset()

        chronometerWidget.base = SystemClock.elapsedRealtime()
        chronometerLogWidget.base = SystemClock.elapsedRealtime()
        chronometerLogCurrent.base = SystemClock.elapsedRealtime()
        chronometerLogPause.base = SystemClock.elapsedRealtime()

        chronometerLogCurrent.stop()
        chronometerWidget.stop()
        chronometerLogWidget.stop()
        chronometerLogPause.stop()

        buttons_frame_2.visibility = View.GONE
        bt_start.visibility = View.VISIBLE
        buttons_lay_restart_save.visibility = View.GONE
        bt_lap.visibility = View.VISIBLE
        bt_pause.visibility = View.GONE
        bt_resume.visibility = View.GONE
        layout_chronometer_pause.visibility = View.INVISIBLE
        chronometer_lap_log.visibility = View.GONE
        text_view_empty.visibility = View.VISIBLE
        chronometerLogPause.setTextColor(ContextCompat.getColor(this, R.color.colorSecondaryText))

        recycler_view.adapter = null

        updateInfoTravelled()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> startActivityForResult(Intent(this, SettingsActivity::class.java), REQUEST_SETTINGS)
            R.id.menu_activities -> startActivity(Intent(this, ActivitiesActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        configureHeadsetButtons()
    }

    private fun configureHeadsetButtons() {
        if (getPreferences(Context.MODE_PRIVATE).getBoolean(getString(R.string.pref_headset_buttons_name),
                        resources.getBoolean(R.bool.pref_headset_buttons_default_value))) {
            if (null == headsetButtonReceiver) headsetButtonReceiver = HeadsetButtonControl()
            headsetButtonReceiver?.registerHeadsetButton(this, this)
        }
    }

    override fun onPause() {
        headsetButtonReceiver?.unregisterHeadsetButton(this)
        super.onPause()
    }

    override fun btHeadsetHookPressed(keyEvent: KeyEvent?) {
        when {
            View.VISIBLE == bt_start.visibility -> btStartPressed()
            View.VISIBLE == bt_resume.visibility -> btResumePressed()
            View.VISIBLE == bt_lap.visibility -> btLapPressed()
        }
    }

    override fun btHeadsetHookDoubleClickPressed(keyEvent: KeyEvent?) {
        if (View.VISIBLE == bt_pause.visibility) btPausePressed()
    }

    override fun btHeadsetMediaNextPressed(keyEvent: KeyEvent?) {
    }

    override fun btHeadsetMediaPreviousPressed(keyEvent: KeyEvent?) {
    }

    override fun btHeadsetUndefinedPressed(keyEvent: KeyEvent?) {
    }

    override fun onBackPressed() {
        super.finish()
    }

}
