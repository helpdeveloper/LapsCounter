package br.com.helpdev.lapscounter

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.helpdev.lapscounter.ads.InterstitialActivity
import br.com.helpdev.lapscounter.ads.InterstitialAd
import br.com.helpdev.lapscounter.utils.isFirstOpen
import br.com.helpdev.lapscounter.utils.setFirstOpen
import kotlinx.android.synthetic.main.activity_activities.*

class ActivitiesActivity : InterstitialActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}