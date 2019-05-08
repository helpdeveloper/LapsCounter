package br.com.helpdev.lapscounter.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import br.com.helpdev.chronometerlib.Chronometer
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("formattedDate")
fun formattedDate(textView: TextView, date: Date) {
    textView.text = SimpleDateFormat("dd/MM/YYYY - HH:mm", Locale.getDefault()).format(date)
}

@BindingAdapter("formattedChronometerTime")
fun formattedChronometerTime(textView: TextView, runningTime: Long) {
    textView.text = Chronometer.getFormattedTime(runningTime)
}

@BindingAdapter("isGoneIfEmpty")
fun isGoneIfEmpty(textView: TextView, text: String) {
    if (text.isEmpty()) textView.visibility = View.GONE
}