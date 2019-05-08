package br.com.helpdev.lapscounter.model.entity

import io.realm.RealmList
import io.realm.RealmObject
import java.io.Serializable

open class ChronometerEntity(
        var pausedTime: Long = 0,
        var runningTime: Long = 0,
        var laps: RealmList<LapEntity> = RealmList()
) : RealmObject(), Serializable {
    override fun toString() = "ChronometerEntity(pausedTime=$pausedTime, runningTime=$runningTime, laps=$laps)"
}