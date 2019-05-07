package br.com.helpdev.lapscounter.ui

import android.os.Bundle
import br.com.helpdev.lapscounter.R
import br.com.helpdev.lapscounter.ui.ads.InterstitialActivity
import kotlinx.android.synthetic.main.activity_activities.*

class ActivitiesActivity : InterstitialActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}