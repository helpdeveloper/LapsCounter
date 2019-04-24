package br.com.helpdev.lapscounter

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class LapsCounterApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        Realm.setDefaultConfiguration(getRealmConfiguration())
    }

    private fun getRealmConfiguration() = RealmConfiguration.Builder()
            .name("app.realm")
            .build()
}