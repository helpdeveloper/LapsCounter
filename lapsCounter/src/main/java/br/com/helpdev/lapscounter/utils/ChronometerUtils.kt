package br.com.helpdev.lapscounter.utils

import br.com.helpdev.chronometerlib.Chronometer

object ChronometerUtils {

    fun getPace(chronometer: Chronometer, lapDistance: Float, countLastLap: Boolean): Pair<Long, Long> {
        val distanceTravelled = getDistanceTravelled(chronometer, lapDistance, countLastLap)

        if (distanceTravelled <= 0) return Pair(0, 0)

        var runningTime = 0L

        val cut = countLastLap(chronometer, countLastLap)

        for (x in 0 until (chronometer.getObChronometer().laps.size - cut)) {
            val obLap = chronometer.getObChronometer().laps[x]
            runningTime += obLap.getRunningTime()
        }

        return getPace(runningTime, distanceTravelled)
    }

    fun getPace(runningTime: Long, distanceTravelled: Float): Pair<Long, Long> {
        val pace = ((runningTime * 100) / distanceTravelled).toLong()
        return Pair(pace / 60_000L, (pace % 60_000L) / 1000L)
    }

    fun getDistanceTravelled(chronometer: Chronometer, lapDistance: Float, countLastLap: Boolean): Float {
        val cut = countLastLap(chronometer, countLastLap)
        return (chronometer.getObChronometer().laps.size - cut) * lapDistance
    }

    private fun countLastLap(chronometer: Chronometer, countLastLap: Boolean) =
            if (countLastLap && chronometer.getRunningTime() > 0 && Chronometer.STATUS_STOPPED == chronometer.status)
                0
            else
                1
}
