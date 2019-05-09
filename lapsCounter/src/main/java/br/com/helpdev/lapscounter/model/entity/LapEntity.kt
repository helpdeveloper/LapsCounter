package br.com.helpdev.lapscounter.model.entity

import br.com.helpdev.chronometerlib.objects.ObLap
import io.realm.RealmObject
import java.io.Serializable

fun LapEntity.toObLap(): ObLap {
    val obLap = ObLap()
    obLap.chronometerTime = chronometerTime
    obLap.pausedTime = pausedTime
    obLap.startTime = 0L
    obLap.endTime = runningTime + pausedTime
    return obLap
}

open class LapEntity(
    var chronometerTime: Long = 0,
    var runningTime: Long = 0,
    var pausedTime: Long = 0
) : RealmObject(), Serializable {
    override fun toString(): String {
        return "LapEntity(chronometerTime=$chronometerTime, runningTime=$runningTime, pausedTime=$pausedTime)"
    }
}