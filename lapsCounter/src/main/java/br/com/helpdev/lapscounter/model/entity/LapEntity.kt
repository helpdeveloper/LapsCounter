package br.com.helpdev.lapscounter.model.entity

import io.realm.RealmObject

open class LapEntity(
        var runningTime: Long = 0,
        var pausedTime: Long = 0
) : RealmObject()