package br.com.helpdev.swimlapscounter.chronometer

import android.os.SystemClock
import android.support.annotation.IntDef
import br.com.helpdev.swimlapscounter.chronometer.objects.ObChronometer
import java.io.Serializable

class Chronometer(private var obChronometer: ObChronometer = ObChronometer()) : Serializable {

    @ChronometerStatus
    private var status: Int = STATUS_STOPPED

    private var runningStartBaseTime = 0L
    var pauseBaseTime = 0L
        private set(value) {
            field = value
        }

    fun start(): Long {
        if (STATUS_STARTED == status) return getCurrentBaseTime()
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
        return getCurrentBaseTime()
    }

    fun getCurrentBaseTime(): Long {
        if (pauseBaseTime > 0) {
            return SystemClock.elapsedRealtime() - (runningStartBaseTime + (SystemClock.elapsedRealtime() - pauseBaseTime))
        }
        return SystemClock.elapsedRealtime() - runningStartBaseTime
    }


    fun stop(): Long {
        if (STATUS_STOPPED == status) return getCurrentBaseTime()
        status = STATUS_STOPPED
        pauseBaseTime = SystemClock.elapsedRealtime()
        obChronometer.setEndTime(pauseBaseTime)
        return getCurrentBaseTime()
    }

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