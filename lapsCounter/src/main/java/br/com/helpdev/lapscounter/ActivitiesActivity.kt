package br.com.helpdev.lapscounter

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import br.com.helpdev.lapscounter.ads.InterstitialAd
import br.com.helpdev.lapscounter.utils.isFirstOpen
import br.com.helpdev.lapscounter.utils.setFirstOpen
import kotlinx.android.synthetic.main.activity_activities.*

class ActivitiesActivity : AppCompatActivity() {

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initAd()
    }

    private fun initAd() {
        val interstitialAd = InterstitialAd(this)
        if (!interstitialAd.canDisplayAdByTime(this)) return

        if (isFirstOpen(this, ActivitiesActivity::class.java)) {
            showMessageToOpenAd(interstitialAd)
        } else {
            interstitialAd.show(this)
        }
    }

    private fun showMessageToOpenAd(interstitialAd: InterstitialAd) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.app_name)
        builder.setMessage(R.string.first_show_ad)
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            interstitialAd.show(this)
            setFirstOpen(this, ActivitiesActivity::class.java)
        }
        builder.create().also { dialog = it }.show()
    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
        dialog = null
    }


}