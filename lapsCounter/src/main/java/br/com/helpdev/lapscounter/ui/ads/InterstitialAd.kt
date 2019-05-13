package br.com.helpdev.lapscounter.ui.ads

import android.content.Context
import android.preference.PreferenceManager
import br.com.helpdev.lapscounter.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import java.util.concurrent.TimeUnit

class InterstitialAd(context: Context) {
    companion object {
        private const val SP_LAST_DISPLAY_AD = "last_display_ad"
    }

    private val interstitialAd: InterstitialAd = InterstitialAd(context)

    init {
        interstitialAd.adUnitId = context.getString(R.string.admob_interstitialad_id)
        interstitialAd.loadAd(AdRequest.Builder().build())
    }

    fun show(context: Context) {
        if (interstitialAd.isLoaded) {
            interstitialAd.show()
            setLastDisplayAd(context)
        } else {
            awaitLoadToShow(context)
        }
    }

    private fun awaitLoadToShow(context: Context) {
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                show(context)
            }
        }
    }

    fun canDisplayAdByTime(context: Context): Boolean {
        val long = PreferenceManager.getDefaultSharedPreferences(context).getLong(SP_LAST_DISPLAY_AD, 0)
        return (System.currentTimeMillis() - long) > TimeUnit.MINUTES.toMillis(15)
    }

    private fun setLastDisplayAd(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putLong(SP_LAST_DISPLAY_AD, System.currentTimeMillis()).apply()
    }
}