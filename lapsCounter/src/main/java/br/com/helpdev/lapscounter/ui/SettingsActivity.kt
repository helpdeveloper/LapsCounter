package br.com.helpdev.lapscounter.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.helpdev.lapscounter.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}