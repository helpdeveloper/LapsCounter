package br.com.helpdev.chronometerlib

import android.os.SystemClock
import android.support.annotation.IntDef
import br.com.helpdev.chronometerlib.objects.ObChronometer
import java.io.Serializable
import java.text.DecimalFormat

class Chronometer(private var obChronometer: ObChronometer = ObChronometer()) : Serializable {

    @ChronometerStatus
    var status = STATUS_STOPPED
        private set

    private var runningStartBaseTime = 0L
    private var stopBaseTime = 0L

    private var pauseBaseTime = 0L

    /**
     * Start/Resume the chronometer
     * Return the running time.
     */
    fun start(): Long {
        if (STATUS_STARTED == status) return getRunningTime()
        if (STATUS_STOPPED == status && stopBaseTime > 0) {
            reset()
        }
        status = STATUS_STARTED
        //--
        if (0L == runningStartBaseTime) {
            runningStartBaseTime = SystemClock.elapsedRealtime()
            obChronometer.setStartTime(runningStartBaseTime)
        } else {
            val pausedTime = SystemClock.elapsedRealtime() - pauseBaseTime
            obChronometer.addPausedTime(pausedTime)
            runningStartBaseTime += pausedTime
            pauseBaseTime = 0L
        }
        return getRunningTime()
    }


    /**
     * Pause chronometer
     * Return the running time.
     */
    fun pause(): Long {
        if (STATUS_PAUSED == status) return getRunningTime()
        if (STATUS_STOPPED == status) throw IllegalStateException("Chronometer is stoped!")
        status = STATUS_PAUSED
        pauseBaseTime = SystemClock.elapsedRealtime()
        obChronometer.setEndTime(pauseBaseTime)
        return getRunningTime()
    }

    fun stop(): ObChronometer {
        if (STATUS_STOPPED == status) return getObChronometer()
        status = STATUS_STOPPED
        pauseBaseTime = 0L
        stopBaseTime = SystemClock.elapsedRealtime()
        obChronometer.setEndTime(stopBaseTime)
        return getObChronometer()
    }

    /**
     * Reset values of chronometer
     */
    fun reset() {
        obChronometer = ObChronometer()
        runningStartBaseTime = 0L
        pauseBaseTime = 0L
        stopBaseTime = 0L
    }

    fun getObChronometer() = obChronometer

    fun lap() = obChronometer.newLap(SystemClock.elapsedRealtime())

    /**
     * Return the running time.
     */
    fun getRunningTime() = SystemClock.elapsedRealtime() - getCurrentBase()


    /**
     * Return the current base of chronometer widget
     */
    fun getCurrentBase() = when {
        0L == runningStartBaseTime -> SystemClock.elapsedRealtime()
        pauseBaseTime > 0 -> runningStartBaseTime + (SystemClock.elapsedRealtime() - pauseBaseTime)
        else -> runningStartBaseTime
    }

    /**
     * Return the base of pause in the last lap
     */
    fun getLastLapBasePause() = when (pauseBaseTime) {
        0L -> SystemClock.elapsedRealtime() - getObChronometer().laps.last().pausedTime
        else -> pauseBaseTime - getObChronometer().laps.last().pausedTime
    }

    fun getPauseBaseTime() = when (status) {
        STATUS_STOPPED -> pauseBaseTime + (SystemClock.elapsedRealtime() - stopBaseTime)
        else -> pauseBaseTime
    }

    /**
     * Return the base of the last lap
     */
    fun getBaseLastLap() = when (pauseBaseTime) {
        0L -> getObChronometer().laps.last().getBase()
        else -> getObChronometer().laps.last().getBase() + (SystemClock.elapsedRealtime() - pauseBaseTime)
    }

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(STATUS_STARTED, STATUS_PAUSED, STATUS_STOPPED)
    annotation class ChronometerStatus

    companion object {
        const val STATUS_STOPPED = 2
        const val STATUS_STARTED = 1
        const val STATUS_PAUSED = 3

        fun getFormattedTime(timeElapsed: Long): String {
            val df = DecimalFormat("00")

            val hours = (timeElapsed / (3600 * 1000)).toInt()
            var remaining = (timeElapsed % (3600 * 1000)).toInt()

            val minutes = remaining / (60 * 1000)
            remaining %= (60 * 1000)

            val seconds = remaining / 1000
            remaining %= 1000

            val milliseconds = timeElapsed.toInt() % 1000 / 100

            var text = ""

            if (hours > 0) {
                text += df.format(hours.toLong()) + ":"
            }

            text += df.format(minutes.toLong()) + ":"
            text += df.format(seconds.toLong()) + "."
            if (hours <= 0) {
                text += Integer.toString(milliseconds)
            }
            return text
        }
    }
}