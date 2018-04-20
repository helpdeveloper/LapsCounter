package br.com.helpdev.lapscounter

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment

class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings_preference)

        val pref = preferenceManager.findPreference(getString(R.string.pref_lap_distance_name))
        if (null != pref) {
            pref.onPreferenceChangeListener = this
            onPreferenceChange(pref,
                    preferenceManager.sharedPreferences.getString(pref.key, getString(R.string.pref_lap_distance_default_value))
            )
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        when (preference.key) {
            getString(R.string.pref_lap_distance_name) -> preference.summary = getString(R.string.pref_lap_distance_summary, newValue)
        }
        return true
    }

}