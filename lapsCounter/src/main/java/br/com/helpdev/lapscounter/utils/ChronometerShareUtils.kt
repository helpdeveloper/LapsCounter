package br.com.helpdev.lapscounter.utils

import android.content.Context
import android.content.Intent
import br.com.helpdev.chronometerlib.Chronometer
import br.com.helpdev.lapscounter.R
import java.text.SimpleDateFormat
import java.util.*

object ChronometerShareUtils {
    fun shareText(context: Context, chronometer: Chronometer, lapDistance: Float, countLastLap: Boolean) {
        var pausedTime = 0L
        var runningTime = 0L
        val laps = StringBuilder()

        for (x in 0 until chronometer.getObChronometer().laps.size) {
            val lap = chronometer.getObChronometer().laps[x]
            pausedTime += lap.pausedTime
            runningTime += lap.getRunningTime()

            val pace = ChronometerUtils.getPace(lap.getRunningTime(), lapDistance)

            laps.append(
                    context.getString(R.string.text_lap_share,
                            (x + 1),
                            Chronometer.getFormattedTime(lap.getRunningTime()),
                            Chronometer.getFormattedTime(lap.chronometerTime),
                            Chronometer.getFormattedTime(lap.pausedTime),
                            String.format("%02d:%02d", pace.first, pace.second)
                    )
            )
        }
        val pace = ChronometerUtils.getPace(chronometer, lapDistance, countLastLap)

        if (!countLastLap) {
            laps.append(context.getString(R.string.last_lap_dont_count)).append("\n")
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val stringShare = context.getString(R.string.text_to_share,
                sdf.format(chronometer.dateStarted),
                Chronometer.getFormattedTime(runningTime),
                Chronometer.getFormattedTime(pausedTime),
                Chronometer.getFormattedTime(runningTime + pausedTime),
                lapDistance.toString() + "m",
                ChronometerUtils.getDistanceTravelled(chronometer, lapDistance, countLastLap).toString() + "m",
                String.format("%02d:%02d", pace.first, pace.second),
                laps.toString())

        shareText(context, context.getString(R.string.app_name), stringShare)
    }

    private fun shareText(context: Context, subject: String, body: String) {
        val txtIntent = Intent(Intent.ACTION_SEND)
        txtIntent.type = "text/plain"
        txtIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        txtIntent.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(txtIntent, "SHARE"))
    }
}