package br.com.helpdev.lapscounter.model.entity

import br.com.helpdev.chronometerlib.objects.ObLap
import io.realm.RealmList
import io.realm.RealmObject
import java.io.Serializable

fun ChronometerEntity.toObLaps(): List<ObLap> {
    val obLaps = mutableListOf<ObLap>()
    laps.forEach {
        obLaps.add(it.toObLap())
    }
    return obLaps
}

open class ChronometerEntity(
    var pausedTime: Long = 0,
    var runningTime: Long = 0,
    var laps: RealmList<LapEntity> = RealmList()
) : RealmObject(), Serializable {
    override fun toString() = "ChronometerEntity(pausedTime=$pausedTime, runningTime=$runningTime, laps=$laps)"
}