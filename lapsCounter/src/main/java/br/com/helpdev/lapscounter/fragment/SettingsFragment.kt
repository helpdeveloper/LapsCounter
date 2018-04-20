package br.com.helpdev.lapscounter.fragment

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import br.com.helpdev.lapscounter.R

class SettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings_preference)

        val pref = preferenceManager.findPreference(getString(R.string.pref_lap_distance_name))
        if (null != pref) {
            pref.onPreferenceChangeListener = this
            onPreferenceChange(pref,
                    preferenceManager.sharedPreferences.getFloat(pref.key,
                            resources.getInteger(R.integer.pref_lap_distance_default_value).toFloat()
                    )
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