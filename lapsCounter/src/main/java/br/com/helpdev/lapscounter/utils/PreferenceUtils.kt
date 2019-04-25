package br.com.helpdev.lapscounter.utils

import android.content.Context
import android.preference.PreferenceManager
import br.com.helpdev.lapscounter.R

fun getLapDistance(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
        .getFloat(context.getString(R.string.pref_lap_distance_name),
                context.resources.getInteger(R.integer.pref_lap_distance_default_value).toFloat())


fun isAudioEnable(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
        .getBoolean(context.getString(R.string.pref_audio_click_name),
                context.resources.getBoolean(R.bool.pref_audio_click_value))

fun countLastLap(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
        .getBoolean(context.getString(R.string.pref_count_last_lap_name), true)