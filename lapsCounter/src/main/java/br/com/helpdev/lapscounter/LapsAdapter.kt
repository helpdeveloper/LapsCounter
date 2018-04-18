package br.com.helpdev.lapscounter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.helpdev.chronometerlib.objects.ObChronometer
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class LapsAdapter(private val context: Context, private val obChronometer: ObChronometer) : RecyclerView.Adapter<LapsAdapter.Companion.ItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(context)
        return ItemHolder(inflater.inflate(R.layout.item_lap_log_fix, parent, false))
    }

    override fun getItemCount() = if (obChronometer.laps.size == 0) 0 else obChronometer.laps.size - 1

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val obLap = obChronometer.laps[position]
        holder.numLap.text = context.getString(R.string.num_lap, String.format("%02d", position + 1))
        holder.tvTime.text = format(obLap.getRunningTime())
        if (obLap.pausedTime > 0) {
            holder.tvTotalPauseTime.text = format(obLap.pausedTime)
            holder.tvTotalPauseTime.visibility = View.VISIBLE
        } else {
            holder.tvTotalPauseTime.visibility = View.INVISIBLE
        }
        holder.tvTotalTime.text = format(obLap.chronometerTime)
    }

    private fun format(timeElapsed: Long): String {
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
        text += Integer.toString(milliseconds)
        return text
    }

    companion object {
        private val SDF = SimpleDateFormat("HH:MM:ss.m", Locale.getDefault())

        class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val numLap: TextView = itemView.findViewById(R.id.numberOfLap_fix)
            val tvTime: TextView = itemView.findViewById(R.id.chronometer_current_fix)
            val tvTotalTime: TextView = itemView.findViewById(R.id.chronometer_total_fix)
            val tvTotalPauseTime: TextView = itemView.findViewById(R.id.chronometer_pause_fix)
        }
    }
}