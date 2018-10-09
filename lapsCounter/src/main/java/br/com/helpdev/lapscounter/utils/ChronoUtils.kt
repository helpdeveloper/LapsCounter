package br.com.helpdev.lapscounter.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import br.com.helpdev.chronometerlib.Chronometer
import br.com.helpdev.lapscounter.R

object ChronoUtils {

    fun getPace(context: Context, chronometer: Chronometer): Pair<Long, Long> {
        val distanceTravelled = getDistanceTravelled(context, chronometer)

        if (distanceTravelled <= 0) return Pair(0, 0)

        var runningTime = 0L

        val cut = countLastLap(context, chronometer)

        for (x in 0 until (chronometer.getObChronometer().laps.size - cut)) {
            val obLap = chronometer.getObChronometer().laps[x]
            runningTime += obLap.getRunningTime()
        }

        val pace = ((runningTime * 100) / distanceTravelled).toLong()

        return Pair(pace / 60_000L, (pace % 60_000L) / 1000L)
    }

    fun getPace(runningTime: Long, distanceTravelled: Float): Pair<Long, Long> {
        val pace = ((runningTime * 100) / distanceTravelled).toLong()
        return Pair(pace / 60_000L, (pace % 60_000L) / 1000L)
    }

    fun getDistanceTravelled(context: Context, chronometer: Chronometer): Float {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val infoLapValue = sp.getFloat(context.getString(R.string.pref_lap_distance_name), context.resources.getInteger(R.integer.pref_lap_distance_default_value).toFloat())

        val cut = countLastLap(context, chronometer)
        return (chronometer.getObChronometer().laps.size - cut) * infoLapValue
    }

    private fun countLastLap(context: Context, chronometer: Chronometer): Int {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val b = sp.getBoolean(context.getString(R.string.pref_count_last_lap_name), true)

        return if (b && chronometer.getRunningTime() > 0 && Chronometer.STATUS_STOPPED == chronometer.status) {
            0
        } else {
            1
        }
    }

}
