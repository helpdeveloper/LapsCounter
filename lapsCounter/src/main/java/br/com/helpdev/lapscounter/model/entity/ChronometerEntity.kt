package br.com.helpdev.lapscounter.model.entity

import io.realm.RealmList
import io.realm.RealmObject

open class ChronometerEntity(
        var pausedTime: Long = 0,
        var runningTime: Long = 0,
        var laps: RealmList<LapEntity> = RealmList()
) : RealmObject()