package br.com.helpdev.lapscounter.model.entity

import io.realm.RealmObject
import java.io.Serializable

open class LapEntity(
        var runningTime: Long = 0,
        var pausedTime: Long = 0
) : RealmObject(), Serializable {
    override fun toString() = "LapEntity(runningTime=$runningTime, pausedTime=$pausedTime)"
}