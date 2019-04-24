package br.com.helpdev.lapscounter.objects

import android.content.Context
import android.preference.PreferenceManager
import br.com.helpdev.chronometerlib.objects.ObChronometer
import br.com.helpdev.lapscounter.R
import java.io.Serializable
import java.util.*

class ObActivity private constructor(val name: String, val description: String, val obChronometer: ObChronometer,
                                     val dateTime: Date, val lapDistance: Float, val countLastLap: Boolean) : Serializable {
    companion object {
        fun newInstance(context: Context, name: String, description: String, obChronometer: ObChronometer): ObActivity {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            val lapDistance = sp.getFloat(context.getString(R.string.pref_lap_distance_name), context.resources.getInteger(R.integer.pref_lap_distance_default_value).toFloat())
            val countLastLap = sp.getBoolean(context.getString(R.string.pref_count_last_lap_name), true)
            return ObActivity(name, description, obChronometer, Date(), lapDistance, countLastLap)
        }
    }
}