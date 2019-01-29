package br.com.helpdev.lapscounter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
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
import br.com.helpdev.lapscounter.utils.ChronometerUtils
import kotlinx.android.synthetic.main.include_buttons.*
import kotlinx.android.synthetic.main.include_chronometer.*
import kotlinx.android.synthetic.main.include_lap_log.*
import kotlinx.android.synthetic.main.lap_log_chronometers.*
import kotlinx.android.synthetic.main.main_toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


abstract class AbsMainActivity : AppCompatActivity(), HeadsetButtonControl.HeadsetButtonControlListener {

    private lateinit var chronometer: Chronometer
    private var headsetButtonReceiver: HeadsetButtonControl? = null
    private var audioEnable = true

    companion object {
        const val REQUEST_SETTINGS = 15
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        bt_start.setOnClickListener { btStartPressed().run { alarmAsync(R.raw.beep) } }
        bt_resume.setOnClickListener { btResumePressed().run { alarmAsync(R.raw.beep) } }
        bt_lap.setOnClickListener { btLapPressed().run { alarmAsync(R.raw.beep_2) } }
        bt_pause.setOnClickListener { btPausePressed().run { alarmAsync(R.raw.beep) } }
        bt_save.setOnClickListener { btSharePressed() }
        bt_restart.setOnClickListener { btRestartPressed() }
        bt_stop.setOnClickListener { btStopPressed() }

        if (null == savedInstanceState) {
            chronometer = Chronometer()
        } else {
            chronometer = savedInstanceState.getSerializable("chronometer") as Chronometer
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

            refreshBasesTotal()
            if (Chronometer.STATUS_STARTED == chronometer.status) {
                startChronometersWidget()
                if ((SystemClock.elapsedRealtime() - chronometer.getLastLapBasePause()) > 1) {
                    chronometerLogPause.setTextColor(Color.RED)
                    chronometerLogPause.base = chronometer.getLastLapBasePause()
                }
            } else if (Chronometer.STATUS_PAUSED == chronometer.status) {
                startChronometersPause()
            } else if (Chronometer.STATUS_STOPPED == chronometer.status) {
                refreshBasesPaused()
            }
            refreshChronometerLog()
            createAdapter()
        }

        updateParameters()
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

        recycler_view.also {
            it.adapter?.apply {
                notifyDataSetChanged()
                it.smoothScrollToPosition(itemCount)
            }
        }
        updateInfoTravelled()
    }


    private fun alarmAsync(rawId: Int) {
        if (!audioEnable) return
        thread {
            MediaPlayer.create(this, rawId).apply {
                if (Build.VERSION.SDK_INT >= 21) {
                    setAudioAttributes(AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build())
                } else {
                    setAudioStreamType(AudioManager.STREAM_ALARM)
                }

            }.start()
        }
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

    private fun btSharePressed() {
        chronometer.getObChronometer()

        var pausedTime = 0L
        var runningTime = 0L
        val laps = StringBuilder()
        val lapDistance = PreferenceManager.getDefaultSharedPreferences(this).getFloat(getString(R.string.pref_lap_distance_name), resources.getInteger(R.integer.pref_lap_distance_default_value).toFloat())

        for (x in 0 until chronometer.getObChronometer().laps.size) {
            val lap = chronometer.getObChronometer().laps[x]
            pausedTime += lap.pausedTime
            runningTime += lap.getRunningTime()

            val pace = ChronometerUtils.getPace(lap.getRunningTime(), lapDistance)

            laps.append(
                    getString(R.string.text_lap_share,
                            (x + 1),
                            Chronometer.getFormattedTime(lap.getRunningTime()),
                            Chronometer.getFormattedTime(lap.chronometerTime),
                            Chronometer.getFormattedTime(lap.pausedTime),
                            String.format("%02d:%02d", pace.first, pace.second)
                    )
            )
        }
        val pace = ChronometerUtils.getPace(this, chronometer)

        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val b = sp.getBoolean(getString(R.string.pref_count_last_lap_name), true)
        if (!b) {
            laps.append(getString(R.string.last_lap_dont_count)).append("\n")
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val stringShare = getString(R.string.text_to_share,
                sdf.format(chronometer.dateStarted),
                Chronometer.getFormattedTime(runningTime),
                Chronometer.getFormattedTime(pausedTime),
                Chronometer.getFormattedTime(runningTime + pausedTime),
                lapDistance.toString() + "m",
                ChronometerUtils.getDistanceTravelled(this, chronometer).toString() + "m",
                String.format("%02d:%02d", pace.first, pace.second),
                laps.toString())

        shareText(getString(R.string.app_name), stringShare)
    }

    private fun shareText(subject: String, body: String) {
        val txtIntent = Intent(android.content.Intent.ACTION_SEND)
        txtIntent.type = "text/plain"
        txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject)
        txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, body)
        startActivity(Intent.createChooser(txtIntent, "SHARE"))
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
//            R.id.menu_activities -> startActivity(Intent(this, ActivitiesActivity::class.java))
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
