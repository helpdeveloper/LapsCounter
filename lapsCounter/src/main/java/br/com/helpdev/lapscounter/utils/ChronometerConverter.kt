package br.com.helpdev.lapscounter.utils

import br.com.helpdev.chronometerlib.Chronometer
import br.com.helpdev.lapscounter.model.entity.ActivityEntity
import br.com.helpdev.lapscounter.model.entity.LapEntity

fun Chronometer.toActivityEntity(name: String, description: String, lapDistance: Float): ActivityEntity {
    val activityEntity = ActivityEntity()
    activityEntity.name = name
    activityEntity.description = description
    activityEntity.lapDistance = lapDistance
    activityEntity.dateStarted = this.dateStarted

    for (x in 0 until getObChronometer().laps.size) {
        val lap = getObChronometer().laps[x]
        activityEntity.chronometer!!.laps.add(LapEntity(lap.chronometerTime, lap.getRunningTime(), lap.pausedTime))
        activityEntity.chronometer!!.runningTime += lap.getRunningTime()
        activityEntity.chronometer!!.pausedTime += lap.pausedTime
    }

    return activityEntity
}