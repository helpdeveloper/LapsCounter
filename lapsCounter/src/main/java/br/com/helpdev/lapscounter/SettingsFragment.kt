package br.com.helpdev.lapscounter

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment

class SettingsFragment : PreferenceFragment(),Preference.OnPreferenceChangeListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings_preference)
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        return true
    }

}