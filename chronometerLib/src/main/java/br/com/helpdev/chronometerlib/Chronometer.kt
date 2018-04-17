package br.com.helpdev.chronometerlib

import android.os.SystemClock
import android.support.annotation.IntDef
import br.com.helpdev.chronometerlib.objects.ObChronometer
import java.io.Serializable

class Chronometer(private var obChronometer: ObChronometer = ObChronometer()) : Serializable {

    @ChronometerStatus
    private var status = STATUS_STOPPED

    private var runningStartBaseTime = 0L
    var pauseBaseTime = 0L
        private set(value) {
            field = value
        }

    /**
     * Start/Resume the chronometer
     * Return the running time.
     */
    fun start(): Long {
        if (STATUS_STARTED == status) return getRunningTime()
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
     * Stop/Pause chronometer
     * Return the running time.
     */
    fun stop(): Long {
        if (STATUS_STOPPED == status) return getRunningTime()
        status = STATUS_STOPPED
        pauseBaseTime = SystemClock.elapsedRealtime()
        obChronometer.setEndTime(pauseBaseTime)
        return getRunningTime()
    }

    /**
     * Reset values of chronometer
     */
    fun reset() {
        obChronometer = ObChronometer()
        runningStartBaseTime = 0L
        pauseBaseTime = 0L
    }

    fun getObChronometer() = obChronometer

    fun lap() = obChronometer.newLap(SystemClock.elapsedRealtime())

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(STATUS_STARTED, STATUS_STOPPED)
    annotation class ChronometerStatus

    companion object {
        const val STATUS_STARTED = 1
        const val STATUS_STOPPED = 3
    }
}