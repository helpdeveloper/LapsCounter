package br.com.helpdev.lapscounter

import android.app.Application
import com.google.android.gms.ads.MobileAds
import io.realm.Realm
import io.realm.RealmConfiguration

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initAds()
        initRealm()
    }

    private fun initAds() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
    }

    private fun initRealm() {
        Realm.init(this)
        Realm.setDefaultConfiguration(getRealmConfiguration())
    }

    private fun getRealmConfiguration() = RealmConfiguration.Builder()
        .name("app.realm")
        .build()
}