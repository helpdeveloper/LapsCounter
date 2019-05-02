package br.com.helpdev.lapscounter.ads

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import br.com.helpdev.lapscounter.R
import br.com.helpdev.lapscounter.utils.isFirstOpen
import br.com.helpdev.lapscounter.utils.setFirstOpen

abstract class InterstitialActivity : AppCompatActivity() {

    private var adDialogInformation: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAd()
    }

    private fun initAd() {
        val interstitialAd = InterstitialAd(this)
        if (!interstitialAd.canDisplayAdByTime(this)) return

        if (isFirstOpen(this, javaClass)) {
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
            setFirstOpen(this, javaClass)
        }
        builder.create().also { adDialogInformation = it }.show()
    }

    override fun onPause() {
        super.onPause()
        adDialogInformation?.dismiss()
        adDialogInformation = null
    }
}